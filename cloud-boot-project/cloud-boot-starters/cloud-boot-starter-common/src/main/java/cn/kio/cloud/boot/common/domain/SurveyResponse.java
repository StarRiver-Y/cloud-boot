package cn.kio.cloud.boot.common.domain;

import cn.kio.cloud.boot.common.support.SurveyCodeEnum;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
@Builder
@Deprecated
@JsonPropertyOrder(value = {"status", "data", "msg"})
public class SurveyResponse<T> implements Serializable {

    /**
     * 响应信息
     */
    private Integer status;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 错误信息
     */
    private String msg;

    public SurveyResponse(int status, T date, String msg) {
        this.status = status;
        this.data = date;
        this.msg = msg;
    }

    public SurveyResponse(T data) {
        this.data = data;
        this.status = SurveyCodeEnum.SUCCESS.getCode();
    }

    public SurveyResponse(T data, String msg) {
        this.status = SurveyCodeEnum.SUCCESS.getCode();
        this.data = data;
        this.msg = msg;
    }


    public SurveyResponse() {
        this.status = SurveyCodeEnum.SUCCESS.getCode();
    }
}
