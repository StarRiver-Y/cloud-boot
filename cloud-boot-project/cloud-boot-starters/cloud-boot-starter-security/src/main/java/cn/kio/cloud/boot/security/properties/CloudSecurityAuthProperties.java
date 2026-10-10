package cn.kio.cloud.boot.security.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Slf4j
@Getter
@Setter
@ConfigurationProperties(CloudSecurityAuthProperties.PREFIX)
public class CloudSecurityAuthProperties {

    public static final String PREFIX = "cloud.boot.security";

    /**
     * auth 认证地址
     */
    private String authUrl;

    /**
     * 应用客户端
     */
    private String appKey;

    /**
     * 应用密码
     */
    private String appSecret;

}
