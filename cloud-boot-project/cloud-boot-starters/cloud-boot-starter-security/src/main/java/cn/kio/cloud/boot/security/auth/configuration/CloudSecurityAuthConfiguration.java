package cn.kio.cloud.boot.security.auth.configuration;

import cn.kio.cloud.boot.feign.decode.GlobalFeignDecode;
import cn.kio.cloud.boot.feign.decode.GlobalFeignErrorDecoder;
import cn.kio.cloud.boot.security.auth.interceptor.CloudSecurityAuthRequestInterceptor;
import cn.kio.cloud.boot.security.properties.CloudSecurityAuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author kio
 */
@RequiredArgsConstructor
public class CloudSecurityAuthConfiguration {

    private final CloudSecurityAuthProperties cloudSecurityAuthProperties;

    @Bean
    public CloudSecurityAuthRequestInterceptor cloudAuthRequestInterceptor() {
        return new CloudSecurityAuthRequestInterceptor(cloudSecurityAuthProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public GlobalFeignDecode globalFeignDecode() {
        return new GlobalFeignDecode();
    }

    @Bean
    @ConditionalOnMissingBean
    public GlobalFeignErrorDecoder globalErrorDecoder() {
        return new GlobalFeignErrorDecoder();
    }
}
