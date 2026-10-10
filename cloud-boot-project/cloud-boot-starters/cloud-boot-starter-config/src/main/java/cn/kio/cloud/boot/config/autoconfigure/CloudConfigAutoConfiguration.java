package cn.kio.cloud.boot.config.autoconfigure;

import cn.kio.cloud.boot.config.properties.CloudBootConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author kio
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({CloudBootConfigProperties.class})
@ConditionalOnProperty(prefix = CloudBootConfigProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class CloudConfigAutoConfiguration {

}
