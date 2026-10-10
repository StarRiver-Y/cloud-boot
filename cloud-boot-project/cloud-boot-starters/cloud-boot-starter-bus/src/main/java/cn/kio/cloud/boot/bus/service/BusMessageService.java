package cn.kio.cloud.boot.bus.service;

import cn.kio.cloud.boot.bus.model.form.BusAlarmMessageForm;
import cn.kio.cloud.boot.bus.model.form.BusMessageForm;
import cn.kio.cloud.boot.bus.support.BusTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static cn.kio.cloud.boot.bus.sink.CloudBusSink.OUTPUT;

/**
 * @author kio
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BusMessageService {

    @Value("${spring.application.name:}")
    private String appName;

    @Value("${cloud.brand:CHC}")
    private String brand;

    private final StreamBridge streamingBridge;

    public void send(Object msg) {
        BusMessageForm form = new BusMessageForm();
        form.setBrand(brand);
        form.setAppName(appName);
        form.setMsg(msg);
        form.setEventTime(LocalDateTime.now());
        streamingBridge.send(OUTPUT, "kafka", form);
    }

    public void send(String type, Object msg) {
        BusMessageForm form = new BusMessageForm();
        form.setBrand(brand);
        form.setAppName(appName);
        form.setMsg(msg);
        form.setType(type);
        form.setEventTime(LocalDateTime.now());
        streamingBridge.send(OUTPUT, "kafka", form);
    }

    public void sendAlarm(BusAlarmMessageForm busAlarmMessageForm) {
        BusMessageForm form = new BusMessageForm();
        form.setBrand(brand);
        form.setAppName(appName);
        form.setMsg(busAlarmMessageForm);
        form.setType(BusTypeEnum.ALARM.name());
        form.setEventTime(LocalDateTime.now());
        streamingBridge.send(OUTPUT, "kafka", form);
    }
}
