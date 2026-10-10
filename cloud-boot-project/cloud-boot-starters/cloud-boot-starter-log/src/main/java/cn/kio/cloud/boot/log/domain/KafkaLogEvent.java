package cn.kio.cloud.boot.log.domain;


import java.io.Serializable;

/**
 * @author kio
 */
public class KafkaLogEvent implements Serializable {

    /**
     * 服务器 host
     */
    private String host;

    /**
     * 项目端口
     */
    private String port;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 微服务链路追踪 trackId
     */
    private String traceId;

    /**
     * 微服务链路追踪 spanId
     */
    private String spanId;

    /**
     * 环境
     */
    private String active;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 日志级别
     */
    private String level;

    /**
     * 线程名称
     */
    private String threadName;

    /**
     * 方法名
     */
    private String loggerName;

    /**
     * 日志详细信息
     */
    private String message;

    /**
     * 错误堆栈信息
     */
    private String stackTraceMessage;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getSpanId() {
        return spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStackTraceMessage() {
        return stackTraceMessage;
    }

    public void setStackTraceMessage(String stackTraceMessage) {
        this.stackTraceMessage = stackTraceMessage;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"host\":").append(host == null ? "" : "\"").append(host).append(host == null ? "" : "\"");
        sb.append(",\"port\":").append(port == null ? "" : "\"").append(port).append(port == null ? "" : "\"");
        sb.append(",\"applicationName\":").append(applicationName == null ? "" : "\"").append(applicationName).append(applicationName == null ? "" : "\"");
        sb.append(",\"traceId\":").append(traceId == null ? "" : "\"").append(traceId).append(traceId == null ? "" : "\"");
        sb.append(",\"spanId\":").append(spanId == null ? "" : "\"").append(spanId).append(spanId == null ? "" : "\"");
        sb.append(",\"active\":").append(active == null ? "" : "\"").append(active).append(active == null ? "" : "\"");
        sb.append(",\"timestamp\":").append(timestamp);
        sb.append(",\"level\":").append(level == null ? "" : "\"").append(level).append(level == null ? "" : "\"");
        sb.append(",\"threadName\":").append(threadName == null ? "" : "\"").append(threadName).append(threadName == null ? "" : "\"");
        sb.append(",\"loggerName\":").append(loggerName == null ? "" : "\"").append(loggerName).append(loggerName == null ? "" : "\"");
        sb.append(",\"message\":").append(message == null ? "" : "\"").append(message).append(message == null ? "" : "\"");
        sb.append(",\"stackTraceMessage\":").append(stackTraceMessage == null ? "" : "\"").append(stackTraceMessage).append(stackTraceMessage == null ? "" : "\"");
        sb.append('}');
        return sb.toString();
    }
}
