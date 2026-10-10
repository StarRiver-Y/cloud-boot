package cn.kio.cloud.boot.exception;

import cn.kio.cloud.boot.common.domain.CloudResponse;
import cn.kio.cloud.boot.common.support.GlobalErrorCodeEnum;

/**
 * 401 全局业务异常
 *
 * @author kio
 */
public class UnauthorizedRuntimeException extends RuntimeException {

    private CloudResponse<Void> response;

    /**
     * 业务异常通用编码
     */
    private String code = GlobalErrorCodeEnum.AUTH_FAIL.getCode();

    public UnauthorizedRuntimeException() {
        super();
    }

    public UnauthorizedRuntimeException(String message) {
        super(message);
    }

    public UnauthorizedRuntimeException(String code, String message) {
        super(message);
        this.code = code;
    }

    public UnauthorizedRuntimeException(CloudResponse<Void> response) {
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
