package cn.kio.cloud.boot.common.domain;

import cn.kio.cloud.boot.common.support.GlobalErrorCodeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 全局相应状态码
 *
 * @author kio
 */
@Data
@Slf4j
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder(value = {"code", "data", "msg"})
public class CloudResponse<T> implements Serializable {

    /**
     * 响应 状态码
     */
    private String code;

    /**
     * 状态码是 SUCCESS 时，接口响应结果信息
     */
    private T data;

    /**
     * 状态码不是 200 时，异常信息
     */
    private String msg;

    public static <T> CloudResponse<T> success() {
        return CloudResponse.<T>builder().code(GlobalErrorCodeEnum.SUCCESS.getCode()).build();
    }

    public static <T> CloudResponse<T> success(T data) {
        return CloudResponse.<T>builder().code(GlobalErrorCodeEnum.SUCCESS.getCode()).data(data).build();
    }

    public static <T> CloudResponse<T> error(String msg) {
        return CloudResponse.<T>builder().code(GlobalErrorCodeEnum.OTHER_ERROR.getCode()).msg(msg).build();
    }

    public static <T> CloudResponse<T> error(String code, String msg) {
        return CloudResponse.<T>builder().code(code).msg(msg).build();
    }

    @JsonIgnore
    public boolean isSucceed() {
        return GlobalErrorCodeEnum.SUCCESS.getCode().equals(this.code);
    }
}
