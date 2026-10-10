package cn.kio.cloud.boot.bus.sink;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author kio
 */
public interface CloudBusSink {
    String INPUT = "cloud-bus-input";

    String OUTPUT = "cloud-bus-output";

    /**
     * @return input channel.
     */
    @Input(CloudBusSink.INPUT)
    SubscribableChannel input();

    /**
     * @return output channel
     */
    @Output(CloudBusSink.OUTPUT)
    SubscribableChannel output();
}
