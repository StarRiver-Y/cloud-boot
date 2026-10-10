package cn.kio.cloud.boot.bus.service.impl;

import cn.kio.cloud.boot.bus.model.form.BusAlarmMessageForm;
import cn.kio.cloud.boot.bus.service.BusMessageService;
import cn.kio.cloud.boot.bus.support.BusAlarmTypeEnum;
import cn.kio.cloud.boot.bus.service.CloudTripartiteCallAlarmService;
import cn.hutool.core.map.MapUtil;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author kio
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CloudTripartiteCallAlarmServiceImpl implements CloudTripartiteCallAlarmService {

    private final static Integer ALARM_LEVEL = 1;

    @Value("${cloud.boot.feign.error-alarm.wechat-robot-key:}")
    private String wechatRobotKey;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.profiles.active}")
    private String active;

    private final Tracer tracer;

    private final BusMessageService busMessageService;

    @Override
    public void sendAlarm(String msg) {
        sendAlarm(null, null, msg);
    }

    @Override
    public void sendAlarm(Integer level, String msg) {
        sendAlarm(level, null, msg);
    }

    @Override
    public void sendAlarm(String url, String msg) {
        sendAlarm(null, url, msg);
    }

    @Override
    public void sendAlarm(Integer level, String url, String msg) {
        try {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("appName", appName);
            map.put("active", active);
            map.put("alarmTime", LocalDateTime.now());
            if (StringUtils.isNotBlank(url)) {
                map.put("url", url);
            }

            Span span = tracer.currentSpan();
            if (null != span) {
                TraceContext context = span.context();
                if (null != context) {

                    map.put("traceId", context.traceId());
                    map.put("spanId", context.spanId());
                }
            }

            map.put("msg", msg);
            BusAlarmMessageForm busAlarmMessageForm = BusAlarmMessageForm.builder()
                    .type(BusAlarmTypeEnum.WECHAT_ROBOT.name())
                    .wechatRobotKey(wechatRobotKey)
                    .level(null == level ? ALARM_LEVEL : level)
                    .msg(MapUtil.join(map, "\n", ": ")).build();
            log.info("BusAlarmMessageForm: {}", busAlarmMessageForm);
            if (null != this.busMessageService) {
                this.busMessageService.sendAlarm(busAlarmMessageForm);
            }

        } catch (Exception ex) {
            log.error("process alarm error.", ex);
        }
    }
}
