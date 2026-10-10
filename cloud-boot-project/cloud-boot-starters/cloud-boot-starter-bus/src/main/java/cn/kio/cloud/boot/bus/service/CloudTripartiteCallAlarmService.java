package cn.kio.cloud.boot.bus.service;

public interface CloudTripartiteCallAlarmService {

    /**
     * 发送接口调用异常
     *
     * @param msg 异常信息
     */
    void sendAlarm(String msg);

    /**
     * 发送接口调用异常
     * @param level 级别
     * @param msg 消息
     */
    void sendAlarm(Integer level, String msg);

    /**
     * 发送接口调用异常
     * @param url 调用 url
     * @param msg 消息
     */
    void sendAlarm(String url, String msg);

    /**
     *  发送接口调用异常
     * @param level 级别
     * @param url 调用 url
     * @param msg 消息
     */
    void sendAlarm(Integer level, String url, String msg);
}
