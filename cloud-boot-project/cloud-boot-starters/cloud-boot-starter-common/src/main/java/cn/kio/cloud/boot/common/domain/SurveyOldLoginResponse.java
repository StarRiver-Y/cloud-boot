package cn.kio.cloud.boot.common.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 老的云服务全局响应 已经过期了,不建议使用,仅用做兼容
 *
 * @author kio
 */
@Data
@Slf4j
@Builder
@Deprecated
public class SurveyOldLoginResponse<T> implements Serializable {

    @JsonProperty("Cmd")
    private String Cmd;

    @JsonProperty("Code")
    private String Code;

    @JsonProperty("Message")
    private String Message;

    @JsonProperty("Result")
    private String Result;

    @JsonProperty("Data")
    public T Data;

    @JsonProperty("User")
    private Object User;

    @JsonProperty("LoginDeviceID")
    private String LoginDeviceID;
}
