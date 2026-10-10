package cn.kio.cloud.boot.bus.service;

import cn.kio.cloud.boot.bus.model.form.BusMessageForm;
import cn.kio.cloud.boot.common.util.JsonUtils;

/**
 * @author kio
 */
public interface BusMessageConsumerService {

    default void init(String message) {
        BusMessageForm messageForm = JsonUtils.toBean(message, BusMessageForm.class);
        this.channelMessage(messageForm);
    }

    /**
     * 数据处理
     *
     * @param messageForm
     */
    void channelMessage(BusMessageForm messageForm);
}
