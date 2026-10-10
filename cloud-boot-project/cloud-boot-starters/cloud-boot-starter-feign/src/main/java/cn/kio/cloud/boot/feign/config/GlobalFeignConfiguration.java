package cn.kio.cloud.boot.feign.config;

import cn.kio.cloud.boot.feign.decode.GlobalFeignDecode;
import cn.kio.cloud.boot.feign.decode.GlobalFeignErrorDecoder;
import cn.kio.cloud.boot.feign.interceptor.GlobalFeignInterceptor;
import feign.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * @author kio
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class GlobalFeignConfiguration implements Ordered {

    @Bean
    @ConditionalOnMissingBean
    public GlobalFeignInterceptor defaultBootstrapConfigManager() {
        return new GlobalFeignInterceptor();
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

    @Bean
    Logger.Level getFeignConfig() {
        return Logger.Level.FULL;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
