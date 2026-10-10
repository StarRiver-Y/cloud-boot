package cn.kio.cloud.boot.bus.configuration;

import cn.kio.cloud.boot.bus.service.BusMessageConsumerService;
import cn.kio.cloud.boot.bus.service.impl.DefaultBusMessageConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author kio
 */
@Slf4j
@Configuration
public class BusConfiguration {

    @Bean
    @Order(1)
    @ConditionalOnMissingBean(BusMessageConsumerService.class)
    public BusMessageConsumerService busMessageConsumerService() {
        log.info("[boot] loading default bus message consumer ");
        return new DefaultBusMessageConsumerService();
    }
}
