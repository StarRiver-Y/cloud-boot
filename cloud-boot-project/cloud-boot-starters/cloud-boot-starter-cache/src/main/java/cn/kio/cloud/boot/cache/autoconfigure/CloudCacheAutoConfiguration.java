package cn.kio.cloud.boot.cache.autoconfigure;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kio
 */
@EnableCaching
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({CloudLocalCacheProperties.class, CloudRemoteCacheProperties.class})
@ConditionalOnProperty(prefix = CloudLocalCacheProperties.PREFIX, name = "enabled", havingValue = "true")
public class CloudCacheAutoConfiguration {

    @Bean
    @Primary
    public CacheManager cacheManager(CloudLocalCacheProperties cloudLocalCacheProperties, CloudRemoteCacheProperties cloudRemoteCacheProperties, RedisConnectionFactory redisConnectionFactory) {
        CompositeCacheManager cacheManager = new CompositeCacheManager();
        List<CacheManager> cacheManagers = new ArrayList<>();
        if (cloudLocalCacheProperties.isEnabled()) {
            cacheManagers.add(caffeineCacheManager(cloudLocalCacheProperties));
        }

        if (cloudRemoteCacheProperties.isEnabled() && "redis".equals(cloudRemoteCacheProperties.getType())) {
            cacheManagers.add(redisCacheManager(cloudRemoteCacheProperties, redisConnectionFactory));
        }
        cacheManager.setCacheManagers(cacheManagers);
        cacheManager.setFallbackToNoOpCache(true);
        return cacheManager;
    }

    @Bean("caffeineCacheManager")
    @ConditionalOnProperty(prefix = CloudLocalCacheProperties.PREFIX, name = "enabled", havingValue = "true")
    public CaffeineCacheManager caffeineCacheManager(CloudLocalCacheProperties cloudLocalCacheProperties) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        Cache<Object, Object> cache = Caffeine.newBuilder()
                .initialCapacity(cloudLocalCacheProperties.getInitialCapacity())
                .maximumSize(cloudLocalCacheProperties.getMaximumSize())
                .expireAfterWrite(Duration.ofMinutes(cloudLocalCacheProperties.getExpire()))
                .build();
        cacheManager.registerCustomCache("cloud-boot-local-cache", cache);
        return cacheManager;
    }


    @Bean("redisCacheManager")
    @ConditionalOnProperty(prefix = CloudRemoteCacheProperties.PREFIX, name = "enabled", havingValue = "true")
    public RedisCacheManager redisCacheManager(CloudRemoteCacheProperties cloudRemoteCacheProperties, RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMillis(cloudRemoteCacheProperties.getExpire()))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }

}
