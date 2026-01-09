package com.github.zxs1994.java_template.config.security;

import com.github.zxs1994.java_template.common.ApiResponse;
import com.github.zxs1994.java_template.config.jwt.JwtAuthenticationFilter;
import com.github.zxs1994.java_template.config.jwt.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.github.zxs1994.java_template.mapper.UserMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
@EnableAutoConfiguration(exclude = {UserDetailsServiceAutoConfiguration.class})
public class SecurityConfig {

    // 注册密码加密 Bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final SecurityProperties securityProperties;

    private final PermissionFilter permissionFilter;

    private final UserMapper userMapper;

    public SecurityConfig(SecurityProperties securityProperties, PermissionFilter permissionFilter, UserMapper userMapper) {
        this.securityProperties = securityProperties;
        this.permissionFilter = permissionFilter;
        this.userMapper = userMapper;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
            JwtUtils jwtUtils,
            ObjectMapper objectMapper) throws Exception {

        // 白名单
        String[] urls = securityProperties.getPermitUrls().toArray(new String[0]);


        // 创建 JWT 过滤器实例
        JwtAuthenticationFilter jwtFilter =
                new JwtAuthenticationFilter(jwtUtils, objectMapper, userMapper);

        http
                // 禁用 CSRF，因为我们用 JWT
                .csrf(csrf -> csrf.disable())

                // 不使用表单登录或 HTTP Basic
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                // 权限配置
                .authorizeHttpRequests(auth ->
                                auth.requestMatchers(urls).permitAll()
                    .anyRequest().authenticated()
                )

                // JWT 过滤器放在 UsernamePasswordAuthenticationFilter 前
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                .addFilterAfter(permissionFilter, JwtAuthenticationFilter.class)

                // 返回 JSON 而不是默认 HTML 登录页
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) ->
                                handleAuthError(res, objectMapper, e))
                        .accessDeniedHandler((req, res, e) ->
                                handleAuthError(res, objectMapper, e))
                );

        return http.build();
    }

    /**
     * ⭐ 统一权限 / 认证错误输出
     */
    private void handleAuthError(
            HttpServletResponse response,
            ObjectMapper objectMapper,
            Exception e
    ) throws IOException {
        int code = response.getStatus();
//        System.out.println(code);
//        System.out.println(e.getMessage());
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        ApiResponse<?> body = ApiResponse.fail(code);

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }

}