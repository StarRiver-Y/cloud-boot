package cn.kio.cloud.boot.exception;

import cn.kio.cloud.boot.common.domain.CloudResponse;
import cn.kio.cloud.boot.common.support.GlobalErrorCodeEnum;

import java.io.IOException;

/**
 * feign io 务异常
 *
 * @author kio
 */
public class FeignDecodeException extends IOException {

    private CloudResponse<Void> response;

    /**
     * 业务异常通用编码
     */
    private String code = GlobalErrorCodeEnum.OTHER_ERROR.getCode();

    public FeignDecodeException() {
        super();
    }

    public FeignDecodeException(String message) {
        super(message);
    }

    public FeignDecodeException(String code, String message) {
        super(message);
        this.code = code;
    }

    public FeignDecodeException(CloudResponse<Void> response) {
        super(response.getMsg());
        this.response = response;
        this.code = response.getCode();
    }

    public String getCode() {
        return code;
    }

    public CloudResponse<Void> getResponse() {
        return response;
    }
}
