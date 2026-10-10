package cn.kio.cloud.boot.exception;

import cn.kio.cloud.boot.common.domain.CloudResponse;
import cn.kio.cloud.boot.common.support.GlobalErrorCodeEnum;

/**
 * 全局业务异常
 *
 * @author kio
 */
public class ServiceRuntimeException extends RuntimeException {

    private CloudResponse<Void> response;

    /**
     * 业务异常通用编码
     */
    private String code = GlobalErrorCodeEnum.OTHER_ERROR.getCode();

    public ServiceRuntimeException() {
        super();
    }

    public ServiceRuntimeException(String message) {
        super(message);
    }

    public ServiceRuntimeException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceRuntimeException(CloudResponse<Void> response) {
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
