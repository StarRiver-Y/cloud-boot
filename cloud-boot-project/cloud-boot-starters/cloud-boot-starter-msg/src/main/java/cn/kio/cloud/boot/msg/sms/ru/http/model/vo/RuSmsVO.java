package cn.kio.cloud.boot.msg.sms.ru.http.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;


/**
 * @author kio
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude
@JsonIgnoreProperties(ignoreUnknown = true)
public class RuSmsVO implements Serializable {

    /**
     * the message ID sent by the Client or assigned automatically by the Server.
     */
    private Integer id;

    /**
     * the number of parts (when sending an SMS message) or the number of seconds (when sending a voice message (call)).
     */
    private Integer cnt;

    /**
     * 错误描述
     */
    private String error;

    /**
     * 消费金额
     */
    private String cost;

    /**
     * 余额
     */
    private String balance;

    /**
     * 错误编码
     */
    @JsonProperty("error_code")
    private Integer errorCode;


}
