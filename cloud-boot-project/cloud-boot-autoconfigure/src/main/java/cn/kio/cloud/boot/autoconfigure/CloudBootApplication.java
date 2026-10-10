package cn.kio.cloud.boot.autoconfigure;

import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author kio
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public @interface CloudBootApplication {

    @AliasFor(annotation = EnableAutoConfiguration.class) Class<?>[] exclude() default {};

    @AliasFor(annotation = EnableAutoConfiguration.class) String[] excludeName() default {};

    @AliasFor(annotation = ComponentScan.class, attribute = "basePackages") String[] scanBasePackages() default {"cn.kio.cloud.*"};

    @AliasFor(annotation = ComponentScan.class, attribute = "basePackageClasses") Class<?>[] scanBasePackageClasses() default {};

    @AliasFor(annotation = ComponentScan.class, attribute = "nameGenerator") Class<? extends BeanNameGenerator> nameGenerator() default BeanNameGenerator.class;

    @AliasFor(annotation = Configuration.class) boolean proxyBeanMethods() default true;


    @AliasFor(annotation = EnableDiscoveryClient.class) boolean autoRegister() default true;

    @AliasFor(annotation = EnableFeignClients.class, attribute = "basePackages") String[] baseFeignPackages() default {"cn.kio.cloud.**.provider", "cn.kio.cloud.**.http"};
}
