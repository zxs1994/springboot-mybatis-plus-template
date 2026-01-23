package com.github.zxs1994.java_template.config.security;

import com.github.zxs1994.java_template.config.AuthLevelResolver;
import com.github.zxs1994.java_template.entity.SysPermission;
import com.github.zxs1994.java_template.enums.AuthLevel;
import com.github.zxs1994.java_template.mapper.SysPermissionMapper;

import com.github.zxs1994.java_template.util.CurrentUser;
import com.github.zxs1994.java_template.util.SysPermissionMatcher;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SysPermissionFilter extends OncePerRequestFilter {

    private final SysPermissionMapper sysPermissionMapper;
    private final AuthLevelResolver authLevelResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        AuthLevel authLevel = authLevelResolver.resolve(path);

        // 1️⃣ 白名单直接放行
        if (authLevel == AuthLevel.WHITELIST) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2️⃣ 需要登录（LOGIN_ONLY / NORMAL / PLATFORM_ONLY）
        if (!CurrentUser.isLogin()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 3️⃣ 平台专属接口：仅平台用户可访问
        if (authLevel == AuthLevel.PLATFORM_ONLY && !CurrentUser.isPlatformUser()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // 4️⃣ 登录即可
        if (authLevel == AuthLevel.LOGIN_ONLY) {
            filterChain.doFilter(request, response);
            return;
        }

        // 5️⃣ NORMAL：需要权限校验
        Long userId = CurrentUser.getUserId();
        List<SysPermission> userPermissions = sysPermissionMapper.selectByUserId(userId);

        SysPermission matched = userPermissions.stream()
                .filter(p -> SysPermissionMatcher.match(p, method, path))
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