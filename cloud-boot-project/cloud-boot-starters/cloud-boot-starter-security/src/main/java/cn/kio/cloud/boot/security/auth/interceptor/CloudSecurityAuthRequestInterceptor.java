package cn.kio.cloud.boot.security.auth.interceptor;

import cn.kio.cloud.boot.common.util.CheckSumUtils;
import cn.kio.cloud.boot.security.properties.CloudSecurityAuthProperties;
import cn.hutool.core.util.IdUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

/**
 * @author kio
 */
@Slf4j
public class CloudSecurityAuthRequestInterceptor implements RequestInterceptor {

    private final CloudSecurityAuthProperties cloudSecurityAuthProperties;

    public CloudSecurityAuthRequestInterceptor(CloudSecurityAuthProperties cloudSecurityAuthProperties) {
        this.cloudSecurityAuthProperties = cloudSecurityAuthProperties;
    }

    @Override
    public void apply(RequestTemplate template) {
        String nonce = IdUtil.fastSimpleUUID();
        String curTime = String.valueOf(Instant.now().toEpochMilli() / 1000);
        template.header("AppKey", cloudSecurityAuthProperties.getAppKey());
        template.header("Nonce", nonce);
        template.header("CurTime", curTime);
        template.header("CheckSum", CheckSumUtils.getCheckSum(cloudSecurityAuthProperties.getAppSecret(), nonce, curTime));
    }
}
