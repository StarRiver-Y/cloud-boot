package cn.kio.cloud.boot.feign.decode;

import cn.kio.cloud.boot.common.domain.CloudResponse;
import cn.kio.cloud.boot.common.support.GlobalErrorCodeEnum;
import cn.kio.cloud.boot.common.util.JsonUtils;
import cn.kio.cloud.boot.exception.ServiceRuntimeException;
import cn.hutool.core.exceptions.ExceptionUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

/**
 * 全局 feign 组件间调用异常响应解码
 *
 * @author kio
 */
@Slf4j
public class GlobalFeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        String errorMessage = "";
        try {
            log.info("methodKey: {} request: {}  response: {}", methodKey, response.request(), response);
            if (response.body() != null) {
                errorMessage = Util.toString(response.body().asReader(Util.UTF_8));
                log.error("errorMessage: {}", errorMessage);
                sendAlarm(response.request().url(), errorMessage);
                if (response.status() == HttpStatus.SERVICE_UNAVAILABLE.value()) {
                    return new ServiceRuntimeException(GlobalErrorCodeEnum.RPC_CALL_NOT_FOUND_ERROR.getCode(), errorMessage);
                } else if (response.status() == HttpStatus.BAD_REQUEST.value() || response.status() == HttpStatus.UNAUTHORIZED.value() || response.status() == HttpStatus.FORBIDDEN.value()) {
                    CloudResponse<Void> res = JsonUtils.toBean(errorMessage, new TypeReference<>() {
                    });
                    assert res != null;
                    return new ServiceRuntimeException(res);
                }
            }
        } catch (Exception e) {
            errorMessage = ExceptionUtil.getMessage(e);
            log.error("FeignClientErrorDecoder decode exception:{}.", errorMessage, e);
            sendAlarm(response.request().url(), errorMessage);
            return new ServiceRuntimeException(GlobalErrorCodeEnum.RPC_CALL_ERROR.getCode(), e.getMessage());
        }
        log.error("methodKey: {}, status: {}", methodKey, response.status());
        sendAlarm(response.request().url(), errorMessage);
        return new ServiceRuntimeException(GlobalErrorCodeEnum.OTHER_ERROR.getCode(), "服务端未知异常！");
    }

    private void sendAlarm(String url, String msg) {
        try {
            //todo
        } catch (Exception var10) {
            log.error("process alarm error.", var10);
        }
    }
}
