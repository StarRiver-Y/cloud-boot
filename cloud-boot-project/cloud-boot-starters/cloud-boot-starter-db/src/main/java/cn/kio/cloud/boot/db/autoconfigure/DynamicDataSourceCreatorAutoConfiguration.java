package cn.kio.cloud.boot.db.autoconfigure;

import cn.kio.cloud.boot.db.creator.DataSourceCreator;
import cn.kio.cloud.boot.db.creator.DefaultDataSourceCreator;
import cn.kio.cloud.boot.db.creator.HikariDataSourceCreator;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * @author kio
 */
@Configuration
public class DynamicDataSourceCreatorAutoConfiguration {


    @Primary
    @Bean
    @ConditionalOnMissingBean
    public DefaultDataSourceCreator dataSourceCreator(List<DataSourceCreator> dataSourceCreators) {
        DefaultDataSourceCreator defaultDataSourceCreator = new DefaultDataSourceCreator();
        defaultDataSourceCreator.setCreators(dataSourceCreators);
        return defaultDataSourceCreator;
    }

    /**
     * 存在Hikari数据源时, 加入创建器
     */
    @ConditionalOnClass(HikariDataSource.class)
    @Configuration
    static class HikariDataSourceCreatorConfiguration {
        @Bean
        public HikariDataSourceCreator hikariDataSourceCreator() {
            return new HikariDataSourceCreator();
        }
    }
}
