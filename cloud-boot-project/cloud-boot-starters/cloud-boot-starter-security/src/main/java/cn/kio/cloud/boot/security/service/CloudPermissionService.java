package cn.kio.cloud.boot.security.service;

import cn.kio.cloud.boot.exception.ServiceRuntimeException;
import cn.kio.cloud.boot.security.auth.http.CloudSecurityAuthClient;
import cn.kio.cloud.boot.security.auth.model.form.RequestInfoForm;
import cn.kio.cloud.boot.security.properties.CloudSecurityAuthProperties;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.servlet.ServletUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;

/**
 * 权限校验 service
 *
 * @author kio
 */
@Slf4j
@RequiredArgsConstructor
public class CloudPermissionService {

    private final CloudSecurityAuthProperties cloudSecurityAuthProperties;

    private final CloudSecurityAuthClient cloudSecurityAuthClient;

    public boolean hasAuthority(String permissions) {
        log.info("permissions: {}", permissions);

        if (StringUtils.isBlank(permissions)) {
            throw new ServiceRuntimeException("权限标识不能为空");
        }

        HttpServletRequest request = ((ServletRequestAttributes)
                (RequestContextHolder.currentRequestAttributes())).getRequest();

        if ("inner".equals(request.getHeader("Client-Source"))) {
            return true;
        }

        String userId = ServletUtil.getHeader(request, "UserId", CharsetUtil.CHARSET_ISO_8859_1);

        if (StringUtils.isBlank(userId)) {
            throw new ServiceRuntimeException("用户不能为空");
        }

        RequestInfoForm requestInfoForm = new RequestInfoForm();
        requestInfoForm.setUserId(userId);
        requestInfoForm.setAppKey(cloudSecurityAuthProperties.getAppKey());
        requestInfoForm.setPermissions(permissions);
        requestInfoForm.setHeaders(ServletUtil.getHeaderMap(request));
        requestInfoForm.setQueryParams(request.getParameterMap());

        try {
            ContentCachingRequestWrapper wrapper = org.springframework.web.util.WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
            if (wrapper != null) {
                byte[] buf = wrapper.getContentAsByteArray();
                if (buf.length > 0) {
                    requestInfoForm.setBody(new String(buf, CharsetUtil.CHARSET_UTF_8));
                }
            }
        } catch (Exception e) {
            log.error("get request body error", e);
        }


        return cloudSecurityAuthClient.isAuthority(requestInfoForm);
    }
}
