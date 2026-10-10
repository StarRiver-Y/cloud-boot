package cn.kio.cloud.boot.job.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author kio
 */
@Getter
@Setter
@ConfigurationProperties(prefix = JobProperties.PREFIX)
public class JobProperties {

    public static final String PREFIX = "cloud.boot.job";

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 调度中心部署根地址
     */
    private String addresses;

    /**
     * 执行器通讯TOKEN [选填]：非空时启用
     */
    private String accessToken;
}
