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
@ConfigurationProperties(CloudLocalCacheProperties.PREFIX)
public class CloudLocalCacheProperties implements Serializable {

    public static final String PREFIX = "cloud.boot.cache.local";

    /**
     * 是否启用本地缓存
     */
    private boolean enabled = false;

    /**
     * 本地缓存默认过期时间,默认单位为秒
     **/
    private Long expire = 30L;

    /**
     * 本地缓存最大条数
     **/
    private Long maximumSize = 1000L;

    /**
     * 本地缓存初始大小
     **/
    private Integer initialCapacity = 150;
}
