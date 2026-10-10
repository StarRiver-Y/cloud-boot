package cn.kio.cloud.boot.config.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author kio
 */
@Slf4j
@Getter
@Setter
@ConfigurationProperties(CloudBootConfigProperties.PREFIX)
public class CloudBootConfigProperties {

    public static final String PREFIX = "cloud.boot.config";

    /**
     * 是否启用自动化配置
     */
    private Boolean enabled;

    /**
     * boot 全局依赖配置地址
     */
    private String url;

    /**
     * boot 全局依赖配置账号
     */
    private String username;

    /**
     * boot 全局依赖配置密码
     */
    private String password;

    /**
     * 服务分组
     */
    private String group;

    /**
     * 配置文件默认的格式
     */
    private String fileExtension = "properties";
}
