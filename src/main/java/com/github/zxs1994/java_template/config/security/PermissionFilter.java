package com.github.zxs1994.java_template.config.security;

import com.github.zxs1994.java_template.config.jwt.JwtUtils;
import com.github.zxs1994.java_template.entity.Permission;
import com.github.zxs1994.java_template.mapper.PermissionMapper;

import com.github.zxs1994.java_template.util.CurrentUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class PermissionFilter extends OncePerRequestFilter {

    private final PermissionMapper permissionMapper;
    private final JwtUtils jwtUtils;
    private final SecurityProperties securityProperties;
    private final AntPathMatcher matcher = new AntPathMatcher();

    public PermissionFilter(PermissionMapper permissionMapper,
                            JwtUtils jwtUtils,
                            SecurityProperties securityProperties) {
        this.permissionMapper = permissionMapper;
        this.jwtUtils = jwtUtils;
        this.securityProperties = securityProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        // 1️⃣ 白名单直接放行
        if (securityProperties.getPermitUrls().stream().anyMatch(p -> matcher.match(p, path))) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!CurrentUser.isLogin()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Long userId = CurrentUser.getId();

        // 3️⃣ 查询用户拥有的权限
        List<Permission> userPermissions = permissionMapper.selectByUserId(userId);

        // System.out.println(userPermissions.toString());

        // 先过滤 method
        List<Permission> filteredByMethod = userPermissions.stream()
                .filter(p -> p.getMethod().equals("*") || p.getMethod().equalsIgnoreCase(method))
                .toList();

        // 匹配权限：全局 / 模块总开关 / 动态接口 / 静态接口 都统一用 matcher
        Permission matched = filteredByMethod.stream()
                .filter(p -> p.getPath().equals("*") && p.getModule().equals("ALL") // 全局权限
                        || matcher.match(p.getPath(), path))                        // 其他接口
                .findFirst()
                .orElse(null);

        if (matched != null) {
            System.out.println(matched.toString());
            filterChain.doFilter(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}