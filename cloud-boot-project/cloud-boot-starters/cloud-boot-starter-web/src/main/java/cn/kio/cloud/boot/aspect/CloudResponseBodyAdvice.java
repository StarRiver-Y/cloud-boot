package cn.kio.cloud.boot.aspect;

import cn.kio.cloud.boot.common.annotation.ResponseNotIntercept;
import cn.kio.cloud.boot.common.domain.*;
import cn.kio.cloud.boot.common.util.JsonUtils;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.File;
import java.util.Objects;

/**
 * 响应拦截器
 *
 * @author kio
 */
@ControllerAdvice(basePackages = {"cn.kio.cloud"})
public class CloudResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Value("${management.endpoints.web.base-path:/actuator}")
    private String basePath;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        //若方法上加了@ResponseNotIntercept 则该方法不用做统一的拦截
        if (Objects.requireNonNull(returnType.getMethod()).isAnnotationPresent(ResponseNotIntercept.class)) {
            return false;
        }
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest request, ServerHttpResponse response) {
        if (request.getURI().getPath().startsWith(basePath)) {
            return body;
        }

        if (body instanceof File || body instanceof CloudResponse || body instanceof SurveyResponse || body instanceof SurveyOldResponse || body instanceof SurveyOldLoginResponse) {
            return body;
        }

        CloudResponse<Object> res = CloudResponse.success();
        if (body instanceof Page) {
            res.setData(buildPageInfo((Page<Object>) body));
        } else if (body instanceof String) {
            res.setData(body);
            return JsonUtils.toJson(res);
        } else {
            res.setData(body);
        }

        return res;
    }

    private CloudPage<Object> buildPageInfo(Page<Object> page) {
        CloudPage<Object> basePage = new CloudPage<>();
        basePage.setTotal(page.getTotal());
        basePage.setPage(page.getPageNum());
        basePage.setSize(page.getPageSize());
        basePage.setRecords(page.getResult());

        return basePage;
    }
}
