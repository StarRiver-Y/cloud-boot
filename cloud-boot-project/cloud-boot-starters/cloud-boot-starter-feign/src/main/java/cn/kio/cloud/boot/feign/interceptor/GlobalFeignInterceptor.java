package cn.kio.cloud.boot.feign.interceptor;

import cn.kio.cloud.boot.common.context.CloudTenantContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * feign 全局请求拦截器
 *
 * @author kio
 */
@Slf4j
public class GlobalFeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header("Client-Source", "inner");
        template.header("Tenant-Code", CloudTenantContext.getTenantId());
    }
}
