package cn.kio.cloud.boot.security.annotation;

import cn.kio.cloud.boot.security.configuration.CloudResourceAutoConfiguration;
import cn.kio.cloud.boot.security.configuration.CloudSecurityConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.lang.annotation.*;

/**
 * 资源服务注解
 * @author kio
 */
@Documented
@Inherited
@EnableMethodSecurity
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({CloudResourceAutoConfiguration.class, CloudSecurityConfiguration.class})
@EnableGlobalMethodSecurity(prePostEnabled = true)
public @interface EnableCloudResource {
}
