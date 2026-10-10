package cn.kio.cloud.boot.job.autoconfigure;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author kio
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({JobProperties.class, JobExecutorProperties.class})
@ConditionalOnProperty(prefix = JobProperties.PREFIX, name = "enabled", havingValue = "true")
public class JobAutoConfiguration {

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor(JobProperties properties, JobExecutorProperties executorProperties) {
        log.info("job config init");
        XxlJobSpringExecutor executor = new XxlJobSpringExecutor();
        executor.setAdminAddresses(properties.getAddresses());

        if (null != properties.getAccessToken()) {
            executor.setAccessToken(properties.getAccessToken());
        }

        if (null != executorProperties.getAppName()) {
            executor.setAppname(executorProperties.getAppName());
        }

        if (null != executorProperties.getAddress()) {
            executor.setAddress(executorProperties.getAddress());
        }

        if (null != executorProperties.getIp()) {
            executor.setIp(executorProperties.getIp());
        }

        if (null != executorProperties.getPort()) {
            executor.setPort(executorProperties.getPort());
        }

        if (null != executorProperties.getLogPath()) {
            executor.setLogPath(executorProperties.getLogPath());
        }

        if (null != executorProperties.getLogRetentionDays()) {
            executor.setLogRetentionDays(executorProperties.getLogRetentionDays());
        }
        return executor;
    }
}
