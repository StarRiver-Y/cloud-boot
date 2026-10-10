package cn.kio.cloud.boot.security.auth.http;

import cn.kio.cloud.boot.security.auth.configuration.CloudSecurityAuthConfiguration;
import cn.kio.cloud.boot.security.auth.model.form.RequestInfoForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author kio
 */
@FeignClient(value = "cloud-security-auth-client", url = "${cloud.boot.security.auth-url:}", configuration = {CloudSecurityAuthConfiguration.class})
public interface CloudSecurityAuthClient {
    /**
     * 验证用户客户端权限
     *
     * @param cid         用户 cid
     * @param clientId    接入权限 clientId
     * @param permissions 权限标识
     * @return 是否有权限
     */
    @GetMapping({"/open-api/v1/employees/actions/authority"})
    Boolean isAuthority(@RequestParam("cid") String cid, @RequestParam("clientId") String clientId, @RequestParam("permissions") String permissions);

    /**
     * 验证用户客户端权限
     *
     * @param requestInfoForm 用户、请求信息
     * @return 是否有权限
     */
    @PostMapping({"/open-api/v1/employees/actions/authority"})
    Boolean isAuthority(@RequestBody RequestInfoForm requestInfoForm);
}
