package cn.kio.cloud.boot.security.configuration;

import cn.kio.cloud.boot.security.auth.http.CloudSecurityAuthClient;
import cn.kio.cloud.boot.security.properties.CloudSecurityAuthProperties;
import cn.kio.cloud.boot.security.service.CloudPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


/**
 * @author kio
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(CloudSecurityAuthProperties.class)
public class CloudResourceAutoConfiguration {

    private final CloudSecurityAuthProperties cloudSecurityAuthProperties;

    @Bean("cps")
    public CloudPermissionService cloudPermissionService(CloudSecurityAuthClient cloudSecurityAuthClient) {
        return new CloudPermissionService(cloudSecurityAuthProperties, cloudSecurityAuthClient);
    }
}
