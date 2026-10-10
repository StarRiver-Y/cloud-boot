package cn.kio.cloud.boot.aspect;

import cn.kio.cloud.boot.common.annotation.CreatedBy;
import cn.kio.cloud.boot.common.annotation.CreatedDate;
import cn.kio.cloud.boot.common.annotation.UpdatedBy;
import cn.kio.cloud.boot.common.annotation.UpdatedDate;
import cn.kio.cloud.boot.common.base.BaseEntity;
import cn.kio.cloud.boot.common.support.GlobalErrorCodeEnum;
import cn.kio.cloud.boot.exception.ServiceRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;


/**
 * 自定义实现审计功能
 *
 * @author kio
 */
@Slf4j
@Aspect
@Component
public class AuditAdvice {

    /**
     * 新增数据审计
     */
    @Pointcut("execution(* cn.kio.cloud..*.*Dao.insert(..))")
    public void insert() {
    }

    /**
     * 批量新增数据审计
     */
    @Pointcut("execution(* cn.kio.cloud..*.*Dao.batchInsert(..))")
    public void batchInsert() {
    }

    /**
     * 更新数据审计
     */
    @Pointcut("execution(* cn.kio.cloud..*.*Dao.update(..))")
    public void update() {
    }

    @Before("insert()")
    public void beforeInsert(JoinPoint joinPoint) throws IllegalAccessException {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            Object entity = joinPoint.getArgs()[0];
            auditFieldSet(entity, getAuditUser());
        }
    }

    @Before("batchInsert()")
    public void beforeBatchInsert(JoinPoint joinPoint) throws IllegalAccessException {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            Object entity = joinPoint.getArgs()[0];
            if (!(entity instanceof Collection)) {
                throw new ServiceRuntimeException(GlobalErrorCodeEnum.METHOD_ARG_NOT_VALID.getCode(), "批量插入参数必须使用集合");
            }
            for (Object item : (Collection<?>) entity) {
                auditFieldSet(item, getAuditUser());
            }
        }
    }

    private void auditFieldSet(Object item, String loginUser) throws IllegalAccessException {
        Class<?> superclass = item.getClass().getSuperclass();
        if (!superclass.equals(BaseEntity.class)) {
            throw new ServiceRuntimeException(GlobalErrorCodeEnum.METHOD_ARG_NOT_VALID.getCode(), "实体必须继承 BaseEntity 类");
        }
        Field[] fields = superclass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            if (null != field.getAnnotation(CreatedBy.class) || null != field.getAnnotation(UpdatedBy.class)) {
                field.set(item, loginUser);
                continue;
            }

            if (null != field.getAnnotation(CreatedDate.class) || null != field.getAnnotation(UpdatedDate.class)) {
                setAuditTime(field, item);
            }

        }
    }

    @Before("update()")
    public void beforeUpdate(JoinPoint joinPoint) throws IllegalAccessException {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            Object entity = joinPoint.getArgs()[0];
            Class<?> superclass = entity.getClass().getSuperclass();

            if (!superclass.equals(BaseEntity.class)) {
                throw new ServiceRuntimeException(GlobalErrorCodeEnum.METHOD_ARG_NOT_VALID.getCode(), "实体必须继承 BaseEntity 类");
            }
            Field[] fields = superclass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (null != field.getAnnotation(UpdatedBy.class)) {
                    field.set(entity, getAuditUser());
                    continue;
                }

                if (null != field.getAnnotation(UpdatedDate.class)) {
                    setAuditTime(field, entity);
                }
            }
        }
    }

    public String getAuditUser() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (null == requestAttributes) {
            return "SYSTEM";
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String cid = request.getHeader("UserId");
        if (StringUtils.isNotBlank(cid)) {
            return cid;
        }

        cid = (String) request.getAttribute("UserId");
        if (StringUtils.isNotBlank(cid)) {
            return cid;
        }

        String clientId = request.getHeader("AppKey");

        if (StringUtils.isNotBlank(clientId)) {
            return clientId;
        }

        return "SYSTEM";
    }

    private void setAuditTime(Field field, Object target)
            throws IllegalAccessException {

        Class<?> type = field.getType();
        long epochMilli = Instant.now().toEpochMilli();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault());

        if (type == LocalDateTime.class) {
            field.set(target, localDateTime);
            return;
        }

        if (type == Long.class) {
            field.set(target, epochMilli);
        }
    }
}
