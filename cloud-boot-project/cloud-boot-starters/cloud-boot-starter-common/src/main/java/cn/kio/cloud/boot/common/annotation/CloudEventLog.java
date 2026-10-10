package cn.kio.cloud.boot.common.annotation;

import java.lang.annotation.*;

/**
 * @author kio
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CloudEventLog {

    /**
     * 应用appKey
     */
    String appKey() default "";

    /**
     * 用户cid
     */
    String cid() default "";

    /**
     * 类型
     */
    String type() default "";

    /**
     * 用户所在区域
     */
    String userLocation() default "";
}
