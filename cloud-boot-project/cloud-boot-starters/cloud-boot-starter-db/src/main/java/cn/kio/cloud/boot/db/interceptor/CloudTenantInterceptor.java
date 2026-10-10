package cn.kio.cloud.boot.db.interceptor;

import cn.kio.cloud.boot.common.context.CloudTenantContext;
import cn.kio.cloud.boot.db.annotation.CloudTenantFilter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.update.UpdateSet;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "cloud.boot.tenant.aspect", name = "enabled", havingValue = "true")
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class CloudTenantInterceptor implements Interceptor {

    private static final ConcurrentHashMap<String, TenantFilterInfo> METHOD_CACHE = new ConcurrentHashMap<>(128);

    @Value("${cloud.boot.tenant.aspect.mapped-statement-id.suffix-list:}")
    private List<String> mappedStatementIdSuffixList;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler handler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = SystemMetaObject.forObject(handler);
        String msId = (String) metaObject.getValue("delegate.mappedStatement.id");

        TenantFilterInfo info = METHOD_CACHE.computeIfAbsent(msId, this::resolveTenantInfo);
        if (info == null || info.tenantColumns.length == 0) {
            return invocation.proceed();
        }

        String tenantId = CloudTenantContext.getTenantId();
        if (tenantId == null || tenantId.isEmpty()) {
            return invocation.proceed();
        }

        try {
            String originalSql = handler.getBoundSql().getSql();
            String newSql = buildTenantSql(originalSql, info.tenantColumns, info.ignoreTables, tenantId);
            MetaObject boundSqlMeta = SystemMetaObject.forObject(handler.getBoundSql());
            boundSqlMeta.setValue("sql", newSql);
        } catch (Exception e) {
            log.error("[CloudTenantInterceptor] Failed to inject tenant condition, using original SQL", e);
        }

        return invocation.proceed();
    }

    private TenantFilterInfo resolveTenantInfo(String msId) {
        try {
            String className = msId.substring(0, msId.lastIndexOf('.'));
            String methodName = msId.substring(msId.lastIndexOf('.') + 1);
            Class<?> clazz = Class.forName(className);

            // 先尝试用原始 msId 查找注解
            TenantFilterInfo info = findTenantFilter(clazz, methodName);
            if (info != null) {
                return info;
            }

            for (String mappedStatementIdSuffix : mappedStatementIdSuffixList) {
                // 如果 msId 是 PageHelper count 查询，尝试去掉 _COUNT 后缀
                if (msId.endsWith(mappedStatementIdSuffix)) {
                    String originalMethodName = methodName.substring(0, methodName.lastIndexOf(mappedStatementIdSuffix));
                    info = findTenantFilter(clazz, originalMethodName);
                    if (info != null) {
                        return info;
                    }
                }
            }
        } catch (Exception e) {
            log.error("[CloudTenantInterceptor] Failed to read CloudTenantFilter annotation for {}", msId, e);
        }
        return null;
    }

    /**
     * 查找指定类和方法名中带 @CloudTenantFilter 注解的方法
     */
    private TenantFilterInfo findTenantFilter(Class<?> clazz, String methodName) {
        Method method = Arrays.stream(clazz.getMethods())
                .filter(m -> m.getName().equals(methodName))
                .filter(m -> m.isAnnotationPresent(CloudTenantFilter.class))
                .findFirst()
                .orElse(null);

        if (method != null) {
            CloudTenantFilter tf = method.getAnnotation(CloudTenantFilter.class);
            return new TenantFilterInfo(tf.tenantColumns(), tf.ignoreTables());
        }
        return null;
    }


    private String buildTenantSql(String sql, String[] tenantColumns, Set<String> ignoreTables, String tenantId) {
        try {
            Set<String> ignoreSet = ignoreTables.stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toSet());
            Statement statement = CCJSqlParserUtil.parse(sql);

            if (statement instanceof Select select) {
                processSelectBody(select.getSelectBody(), tenantColumns, ignoreSet, tenantId);
            } else if (statement instanceof Update update) {
                if (!ignoreSet.contains(update.getTable().getName().toLowerCase())) {
                    update.setWhere(buildTenantExpression(update.getTable(), update.getWhere(), tenantColumns, tenantId));
                }
                if (update.getUpdateSets() != null) {
                    for (UpdateSet us : update.getUpdateSets()) {
                        for (Object value : us.getValues()) {
                            if (value instanceof Select sel) {
                                processSelectBody(sel.getSelectBody(), tenantColumns, ignoreSet, tenantId);
                            }
                        }
                    }
                }
            } else if (statement instanceof Delete delete) {
                if (!ignoreSet.contains(delete.getTable().getName().toLowerCase())) {
                    delete.setWhere(buildTenantExpression(delete.getTable(), delete.getWhere(), tenantColumns, tenantId));
                }
            } else if (statement instanceof Insert insert) {
                if (!ignoreSet.contains(insert.getTable().getName().toLowerCase())) {
                    addTenantColumnsToInsert(insert, tenantColumns, tenantId);
                }
            }

            return statement.toString();
        } catch (Exception e) {
            log.error("[CloudTenantInterceptor] SQL parse failed, returning original SQL", e);
            return sql;
        }
    }

    private void processSelectBody(Select body, String[] tenantColumns, Set<String> ignoreTables, String tenantId) {
        if (body instanceof PlainSelect plain) {
            addTenantConditionToFromItem(plain.getFromItem(), plain, tenantColumns, ignoreTables, tenantId);
            if (plain.getJoins() != null) {
                for (Join join : plain.getJoins()) {
                    addTenantConditionToFromItem(join.getRightItem(), plain, tenantColumns, ignoreTables, tenantId);
                }
            }
        } else if (body instanceof ParenthesedSelect parenthesedSelect) {
            if (parenthesedSelect.getSelect() != null) {
                processSelectBody(parenthesedSelect.getSelect(), tenantColumns, ignoreTables, tenantId);
            }
        } else if (body instanceof SetOperationList setOp) {
            for (Select sb : setOp.getSelects()) {
                processSelectBody(sb, tenantColumns, ignoreTables, tenantId);
            }
        }
    }

    private void addTenantConditionToFromItem(FromItem fromItem, PlainSelect plainSelect,
                                              String[] tenantColumns, Set<String> ignoreTables, String tenantId) {
        if (!(fromItem instanceof Table table)) {
            return;
        }
        if (ignoreTables.contains(table.getName().toLowerCase())) {
            return;
        }

        Expression tenantExp = null;
        for (String col : tenantColumns) {
            String colName;
            if (table.getAlias() != null) {
                colName = table.getAlias().getName() + "." + col;
            } else {
                colName = col;
            }
            Expression colExp = new EqualsTo(new Column(colName), new StringValue(tenantId));
            if (tenantExp == null) {
                tenantExp = colExp;
            } else {
                tenantExp = new AndExpression(tenantExp, colExp);
            }
        }

        if (plainSelect.getWhere() == null) {
            plainSelect.setWhere(tenantExp);
        } else {
            plainSelect.setWhere(new AndExpression(plainSelect.getWhere(), tenantExp));
        }
    }

    private Expression buildTenantExpression(Table table, Expression where, String[] tenantColumns, String tenantId) {
        Expression tenantExp = null;
        for (String col : tenantColumns) {
            String colName;
            if (table.getAlias() != null) {
                colName = table.getAlias().getName() + "." + col;
            } else {
                colName = col;
            }
            Expression colExp = new EqualsTo(new Column(colName), new StringValue(tenantId));
            if (tenantExp == null) {
                tenantExp = colExp;
            } else {
                tenantExp = new AndExpression(tenantExp, colExp);
            }
        }
        if (where == null) {
            return tenantExp;
        } else {
            return new AndExpression(where, tenantExp);
        }
    }

    private void addTenantColumnsToInsert(Insert insert, String[] tenantColumns, String tenantId) {
        if (tenantColumns == null || tenantColumns.length == 0) {
            return;
        }

        for (String col : tenantColumns) {
            insert.getColumns().add(new Column(col));
        }

        Values values = insert.getValues();
        if (values != null) {
            ExpressionList<?> expressions = values.getExpressions();
            boolean multiRowInsert = expressions.getExpressions().stream()
                    .allMatch(ParenthesedExpressionList.class::isInstance);
            if (multiRowInsert) {
                for (Object expression : expressions.getExpressions()) {
                    addTenantValues((ParenthesedExpressionList<?>) expression, tenantColumns, tenantId);
                }
            } else {
                addTenantValues(expressions, tenantColumns, tenantId);
            }
        } else if (insert.getSelect() != null) {
            processSelectBody(insert.getSelect().getSelectBody(), tenantColumns, Collections.emptySet(), tenantId);
        } else {
            throw new IllegalStateException("Unsupported INSERT statement: " + insert);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void addTenantValues(ExpressionList values, String[] tenantColumns, String tenantId) {
        for (int i = 0; i < tenantColumns.length; i++) {
            values.add(new StringValue(tenantId));
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    private record TenantFilterInfo(String[] tenantColumns, Set<String> ignoreTables) {
        TenantFilterInfo(String[] tenantColumns, String[] ignoreTablesArray) {
            this(tenantColumns, Arrays.stream(ignoreTablesArray)
                    .map(String::toLowerCase)
                    .collect(Collectors.toSet()));
        }
    }
}
