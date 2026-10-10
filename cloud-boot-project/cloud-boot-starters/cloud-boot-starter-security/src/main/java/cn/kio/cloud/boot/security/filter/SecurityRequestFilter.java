package cn.kio.cloud.boot.security.filter;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * body 缓存过滤器
 *
 * @author kio
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityRequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getContentType() != null && request.getContentType().startsWith("multipart/")) {
            filterChain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper wrapper = new ContentCachingRequestWrapper(request, 1024 * 1024);
        filterChain.doFilter(wrapper, response);
    }
}
