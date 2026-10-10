package cn.kio.cloud.boot.msg.sms.alibaba.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author kio
 */
@Getter
@Setter
@ConfigurationProperties(prefix = AlibabaSmsProperties.PREFIX)
public class AlibabaSmsProperties {

    public static final String PREFIX = "cloud.boot.sms.alibaba";

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 短信端点
     */
    private String endpoint = "dysmsapi.aliyuncs.com";

    /**
     * 认证 key
     */
    private String accessKey;

    /**
     * 认证秘钥
     */
    private String accessSecret;

    /**
     * 签名信息
     */
    private String signName;

    /**
     * 签名信息国际
     */
    private String signNameInternational;

    /**
     * 区域标识
     */
    private String regionId;

    /**
     * 短信验证模板国内
     */
    private String captchaTemplate;

    /**
     * 短信验证模板国际
     */
    private String captchaTemplateInternational;
}
