package cn.kio.cloud.boot.db.annotation;

import java.lang.annotation.*;

/**
 * @author kio
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DS {

    /**
     * database
     *
     * @return the database you want to switch
     */
    String value();
}
