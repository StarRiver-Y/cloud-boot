package cn.kio.cloud.boot.bus.consumer;

import cn.kio.cloud.boot.bus.service.BusMessageConsumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

/**
 * 全局消费
 *
 * @author kio
 */
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class CloudBusConsumer {

    private final BusMessageConsumerService busMessageConsumerService;

    @Bean
    public Consumer<String> cloudBusConsumer() {
        return busMessageConsumerService::init;
    }

}
