package cn.kio.cloud.boot.bus.consumer;

import cn.kio.cloud.boot.bus.service.BusMessageConsumerService;
import cn.kio.cloud.boot.bus.sink.CloudBusSink;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

/**
 * 全局消费
 *
 * @author kio
 */
@Component
@RequiredArgsConstructor
@EnableBinding(CloudBusSink.class)
public class CloudBusConsumer {

    private final BusMessageConsumerService busMessageConsumerService;

    @StreamListener(CloudBusSink.INPUT)
    public void cloudChannelConsumer(String message) {
        busMessageConsumerService.init(message);
    }

}
