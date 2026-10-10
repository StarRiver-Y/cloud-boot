package cn.kio.cloud.boot.handle;

import cn.kio.cloud.boot.common.domain.CloudResponse;
import cn.kio.cloud.boot.common.support.GlobalErrorCodeEnum;
import cn.kio.cloud.boot.exception.FeignDecodeException;
import cn.kio.cloud.boot.exception.ServiceRuntimeException;
import cn.kio.cloud.boot.exception.UnauthorizedRuntimeException;
import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import java.util.List;

import static cn.kio.cloud.boot.common.support.GlobalErrorCodeEnum.AUTH_FAIL;
import static cn.kio.cloud.boot.common.support.GlobalErrorCodeEnum.SECURITY_FORBIDDEN;

/**
 * 全局响应拦截器
 *
 * @author kio
 */
@Slf4j
@RestControllerAdvice(basePackages = {"cn.kio.cloud"})
public class GlobalExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({MethodArgumentNotValidException.class, ValidationException.class})
    public CloudResponse<Void> validateException(HttpServletRequest request, Exception e) {
        log.error("请求接口：{}, 参数详细异常信息：{}", request.getServletPath(), ExceptionUtil.stacktraceToString(e), e);

        if (e instanceof MethodArgumentNotValidException ex) {
            List<ObjectError> objectErrors = ex.getBindingResult().getAllErrors();
            if (!CollectionUtils.isEmpty(objectErrors)) {
                return buildCloudResponse(GlobalErrorCodeEnum.METHOD_ARG_NOT_VALID.getCode(), objectErrors.get(0).getDefaultMessage());
            }
        }

        return buildCloudResponse(GlobalErrorCodeEnum.METHOD_ARG_NOT_VALID.getCode(), e.getMessage());
    }

    @ExceptionHandler({BindException.class})
    public CloudResponse<Void> bindException(HttpServletRequest request, BindException e) {
        log.error("请求接口：{}, 参数详细异常信息：{}", request.getServletPath(), ExceptionUtil.stacktraceToString(e), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return this.buildCloudResponse(GlobalErrorCodeEnum.METHOD_ARG_NOT_VALID.getCode(), message);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = ServiceRuntimeException.class)
    public CloudResponse<Void> serviceException(HttpServletRequest request, ServiceRuntimeException e) {
        log.error("请求接口：{}, service 详细异常信息：{}", request.getServletPath(), ExceptionUtil.stacktraceToString(e), e);

        if (null != e.getResponse()) {
            return e.getResponse();
        }

        return buildCloudResponse(e.getCode(), e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = AccessDeniedException.class)
    public CloudResponse<Void> accessDeniedException(HttpServletRequest request, AccessDeniedException e) {
        log.error("请求接口：{}, 403 详细异常信息：{}", request.getServletPath(), ExceptionUtil.stacktraceToString(e), e);

        return buildCloudResponse(SECURITY_FORBIDDEN.getCode(), e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = UnauthorizedRuntimeException.class)
    public CloudResponse<Void> unauthorizedRuntimeException(HttpServletRequest request, UnauthorizedRuntimeException e) {
        log.error("请求接口：{}, 401 详细异常信息：{}", request.getServletPath(), ExceptionUtil.stacktraceToString(e), e);

        return buildCloudResponse(AUTH_FAIL.getCode(), e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({Exception.class})
    public CloudResponse<Void> otherException(HttpServletRequest request, Exception e) {
        log.error("请求接口：{}, other 详细异常信息：{}", request.getServletPath(), ExceptionUtil.stacktraceToString(e), e);

        if (e instanceof MissingServletRequestParameterException ex) {
            return this.buildCloudResponse(GlobalErrorCodeEnum.METHOD_ARG_NOT_VALID.getCode(), ex.getMessage());
        } else if (e instanceof MissingRequestHeaderException ex) {
            return this.buildCloudResponse(GlobalErrorCodeEnum.METHOD_ARG_NOT_VALID.getCode(), ex.getMessage());
        } else if (e instanceof HttpMessageNotReadableException ex) {
            return this.buildCloudResponse(GlobalErrorCodeEnum.METHOD_ARG_NOT_VALID.getCode(), "Request parameter invalid");
        } else if (e.getCause() instanceof FeignDecodeException ex) {
            return buildCloudResponse(ex.getCode(), ex.getMessage());
        }

        return this.buildCloudResponse(GlobalErrorCodeEnum.OTHER_ERROR.getCode(), "系统内部异常，请联系服务管理员");
    }

    private CloudResponse<Void> buildCloudResponse(String code, String message) {
        CloudResponse<Void> res = CloudResponse.error(message);
        res.setCode(code);
        log.error("code: {},msg: {}", res.getCode(), res.getMsg());
        return res;
    }
}
