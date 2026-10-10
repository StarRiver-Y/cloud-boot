package cn.kio.cloud.boot.log.append;

import ch.qos.logback.classic.pattern.ExtendedThrowableProxyConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import cn.kio.cloud.boot.log.domain.KafkaLogEvent;
import cn.hutool.core.net.NetUtil;
import cn.hutool.crypto.SecureUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Map;
import java.util.Properties;

/**
 * 日志推送 kafka
 *
 * @author kio
 */
public class KafkaAppender extends AppenderBase<ILoggingEvent> {

    /**
     * 应用 ip
     */
    private String host;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 环境类型
     */
    private String active;

    /**
     * 服务端口
     */
    private String port;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * kafka 上报地址
     */

    private String bootstrapServers;

    /**
     * 上报 topic
     */
    private String topic;

    private Producer<String, String> producer;

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setBootstrapServers(String bootstrapServers) {
        if (StringUtils.isNotBlank(bootstrapServers)) {
            this.bootstrapServers = bootstrapServers;
        }
    }

    @Override
    public void start() {
        super.start();

        if (null == enabled || !enabled) {
            return;
        }

        if (null == this.host) {
            this.host = NetUtil.getLocalhostStr();
        }
        if (null == this.bootstrapServers) {
            return;
        }

        if (null == this.producer) {
            Properties props = new Properties();
            props.put("bootstrap.servers", this.bootstrapServers);
            props.put("retries", 0);
            props.put("acks", "1");
            props.put("batch.size", 163840);
            props.put("linger.ms", 0);
            props.put("buffer.memory", 33554432);
            props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            producer = new KafkaProducer<>(props);
        }
    }

    @Override
    protected void append(ILoggingEvent loggingEvent) {
        if (null == this.producer) {
            return;
        }
        if (StringUtils.isBlank(this.topic)) {
            return;
        }

        KafkaLogEvent kafkaLogEvent = buildKafkaLogEvent(loggingEvent);
        producer.send(new ProducerRecord<>(topic, SecureUtil.md5(this.applicationName), kafkaLogEvent.toString()));

    }

    private KafkaLogEvent buildKafkaLogEvent(ILoggingEvent loggingEvent) {
        Map<String, String> mdcPropertyMap = loggingEvent.getMDCPropertyMap();

        KafkaLogEvent event = new KafkaLogEvent();
        event.setHost(this.host);
        event.setPort(this.port);
        event.setApplicationName(this.applicationName);
        event.setTraceId(mdcPropertyMap.get("traceId"));
        event.setSpanId(mdcPropertyMap.get("spanId"));
        event.setActive(this.active);
        event.setTimestamp(loggingEvent.getTimeStamp());
        event.setLevel(loggingEvent.getLevel().toString());
        event.setThreadName(loggingEvent.getThreadName());
        event.setLoggerName(loggingEvent.getLoggerName());
        event.setMessage(loggingEvent.getFormattedMessage());
        event.setStackTraceMessage(getStackTraceMessage(loggingEvent));


        return event;
    }

    private String getStackTraceMessage(ILoggingEvent loggingEvent) {

        if (null == loggingEvent) {
            return "";
        }
        ExtendedThrowableProxyConverter throwableConverter = new ExtendedThrowableProxyConverter();
        throwableConverter.start();
        String stackTraceMessage = throwableConverter.convert(loggingEvent);
        throwableConverter.stop();

        return stackTraceMessage;
    }

    @Override
    public void stop() {
        super.stop();
        if (null != this.producer) {
            this.producer.close();
        }
    }
}
