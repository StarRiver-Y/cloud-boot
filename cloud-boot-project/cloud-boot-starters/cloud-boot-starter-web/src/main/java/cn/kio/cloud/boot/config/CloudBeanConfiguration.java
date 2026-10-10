package cn.kio.cloud.boot.config;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

/**
 * @author kio
 */
@Slf4j
@Configuration
public class CloudBeanConfiguration {

    @Bean
    public javax.validation.Validator validator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class).configure().addProperty("hibernate.validator.fail_fast", "true").buildValidatorFactory();

        return validatorFactory.getValidator();
    }
}
