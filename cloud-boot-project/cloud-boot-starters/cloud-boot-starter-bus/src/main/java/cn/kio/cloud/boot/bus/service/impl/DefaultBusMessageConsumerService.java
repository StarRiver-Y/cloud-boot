package cn.kio.cloud.boot.bus.service.impl;

import cn.kio.cloud.boot.bus.model.form.BusMessageForm;
import cn.kio.cloud.boot.bus.service.BusMessageConsumerService;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认实现,打印来源与详细信息
 *
 * @author kio
 */
@Slf4j
public class DefaultBusMessageConsumerService implements BusMessageConsumerService {

    @Override
    public void channelMessage(BusMessageForm messageForm) {
        if (null == messageForm) {
            log.warn("channel message is null");
        }
    }
}

