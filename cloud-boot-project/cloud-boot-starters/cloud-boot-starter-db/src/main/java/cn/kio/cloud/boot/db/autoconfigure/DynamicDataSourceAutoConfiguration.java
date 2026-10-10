package cn.kio.cloud.boot.db.autoconfigure;

import cn.kio.cloud.boot.db.aop.DynamicDataSourceAnnotationAdvisor;
import cn.kio.cloud.boot.db.provider.ChcDynamicDataSourceProvider;
import cn.kio.cloud.boot.db.provider.DynamicDataSourceProvider;
import cn.kio.cloud.boot.db.annotation.DS;
import cn.kio.cloud.boot.db.datasource.DynamicRoutingDataSource;
import cn.kio.cloud.boot.db.interceptor.DynamicDataSourceAnnotationInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 动态数据源自动配置类
 *
 * @author kio
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
@Import(value = {DynamicDataSourceCreatorAutoConfiguration.class})
@ConditionalOnProperty(prefix = DynamicDataSourceProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class DynamicDataSourceAutoConfiguration implements InitializingBean {

    private final DynamicDataSourceProperties properties;

    private final List<DynamicDataSourcePropertiesCustomizer> dataSourcePropertiesCustomizers;

    public DynamicDataSourceAutoConfiguration(DynamicDataSourceProperties properties, ObjectProvider<List<DynamicDataSourcePropertiesCustomizer>> dataSourcePropertiesCustomizers) {
        this.properties = properties;
        this.dataSourcePropertiesCustomizers = dataSourcePropertiesCustomizers.getIfAvailable();
    }

    @Bean
    @Order(0)
    public DynamicDataSourceProvider dynamicDataSourceProvider() {
        return new ChcDynamicDataSourceProvider(properties.getDatasource());
    }

    @Bean
    @ConditionalOnMissingBean
    public DynamicRoutingDataSource dynamicRoutingDataSource() {
        return new DynamicRoutingDataSource();
    }

    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    public Advisor dynamicDatasourceAnnotationAdvisor() {
        DynamicDataSourceAnnotationInterceptor interceptor = new DynamicDataSourceAnnotationInterceptor();
        DynamicDataSourceAnnotationAdvisor advisor = new DynamicDataSourceAnnotationAdvisor(interceptor, DS.class);
        advisor.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return advisor;
    }


    @Override
    public void afterPropertiesSet() {
        if (!CollectionUtils.isEmpty(dataSourcePropertiesCustomizers)) {
            for (DynamicDataSourcePropertiesCustomizer customizer : dataSourcePropertiesCustomizers) {
                customizer.customize(properties);
            }
        }
    }
}
