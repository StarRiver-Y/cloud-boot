package cn.kio.cloud.boot.db.interceptor;


import cn.kio.cloud.boot.db.tool.DataSourceClassResolver;
import cn.kio.cloud.boot.db.tool.DynamicDataSourceContextHolder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author kio
 */
public class DynamicDataSourceAnnotationInterceptor implements MethodInterceptor {

    private final DataSourceClassResolver dataSourceClassResolver;

    public DynamicDataSourceAnnotationInterceptor() {
        this.dataSourceClassResolver = new DataSourceClassResolver();
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String dsKey = determineDatasourceKey(invocation);
        DynamicDataSourceContextHolder.set(dsKey);
        try {
            return invocation.proceed();
        } finally {
            DynamicDataSourceContextHolder.clear();
        }
    }

    private String determineDatasourceKey(MethodInvocation invocation) {
        return dataSourceClassResolver.findKey(invocation.getMethod(), invocation.getThis());
    }
}
