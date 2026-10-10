package cn.kio.cloud.boot.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author kio
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CloudTenantFilter {

    /**
     * 支持多列 例如 "tenant_code", "tenant_id"
     */
    String[] tenantColumns() default {"tenant_code"};

    /**
     * 忽略租户条件的表名
     */
    String[] ignoreTables() default {};
}
