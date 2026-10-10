package cn.kio.cloud.boot.security.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author kio
 */
@Slf4j
@EnableWebSecurity
public class CloudSecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 关闭 csrf
        http.csrf(AbstractHttpConfigurer::disable)
                //不通过 session 获取 SecurityContext
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        //接口匿名访问
                        .requestMatchers("/**").anonymous()
                        //其余接口鉴权
                        .anyRequest().authenticated());
        return http.build();
    }
}
