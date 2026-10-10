package cn.kio.cloud.boot.msg.sms.ru.autoconfigure;

import cn.kio.cloud.boot.msg.sms.ru.http.client.RuSendSmsClient;
import cn.kio.cloud.boot.msg.sms.ru.service.RuSmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 俄罗斯云短信
 *
 * @author kio
 * @
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({RuSmsProperties.class})
@ConditionalOnProperty(prefix = RuSmsProperties.PREFIX, name = "enabled", havingValue = "true")
public class RuSmsAutoConfiguration {

    @Bean
    public RuSmsService ruSmsService(RuSmsProperties properties, RuSendSmsClient ruSendSmsClient) {
        return new RuSmsService(properties,ruSendSmsClient);
    }

}
