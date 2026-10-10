package cn.kio.cloud.boot.cache.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * @author kio
 */
@Slf4j
@Getter
@Setter
@ConfigurationProperties(CloudRemoteCacheProperties.PREFIX)
public class CloudRemoteCacheProperties implements Serializable {

    public static final String PREFIX = "cloud.boot.cache.remote";

    /**
     * 是否启用远程缓存
     */
    private boolean enabled = false;

    /**
     * 远程缓存类型
     */
    private String type = "redis";

    /**
     * 远程缓存默认过期时间,默认单位为秒
     **/
    private Long expire = 30L;
}
