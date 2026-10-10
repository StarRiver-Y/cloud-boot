package cn.kio.cloud.boot.msg.sms.ru.http.model.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


/**
 * @author kio
 */
@Getter
@Setter
public class RuSendSmsForm {

    /**
     * 发送 ak
     */
    private String login;

    /**
     * 发送 sk
     */
    private String psw;

    /**
     * 消息格式化 3-json
     */
    private Integer fmt = 3;

    /**
     * 返回消费情况
     */
    private Integer cost = 3;

    /**
     * 消息编码：koi8-r、windows-1251 或 utf-8（默认）
     */
    private String charset = "utf-8";

    /**
     * 通过逗号或分号分隔的电话号码列表
     */
    private String phones;

    /**
     * 要发送的消息
     */
    private String mes;

    /**
     * 是否将消息内容转译（1、2 或 0）
     * 0: 不进行转译，保持消息内容原样发送。
     * 1: 将消息内容进行简单的转译，例如将特殊字符转换成对应的 ASCII 码。
     * 2: 将消息内容进行更复杂的转译，可能包括将非拉丁字符转换成相似的拉丁字符。
     */
    @JsonProperty("translit")
    private Integer transit = 0;
}
