package cn.kio.cloud.boot.filter;

import cn.kio.cloud.boot.common.context.CloudTenantContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * zongyanan
 *
 * @author kio
 */
@Component
@ConditionalOnProperty(prefix = "cloud.boot.tenant.aspect", name = "enabled", havingValue = "true")
public class CloudTenantFilter extends OncePerRequestFilter {
    private static final String TENANT_HEADER = "Tenant-Code";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String tenantId = request.getHeader(TENANT_HEADER);
            if (StringUtils.isNotBlank(tenantId)) {
                CloudTenantContext.setTenantId(tenantId);
            }
            filterChain.doFilter(request, response);
        } finally {
            // 防止线程池复用导致串数据
            CloudTenantContext.clear();
        }
    }
}
