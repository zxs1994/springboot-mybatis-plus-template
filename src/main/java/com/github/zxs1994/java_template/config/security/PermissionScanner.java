package com.github.zxs1994.java_template.config.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.zxs1994.java_template.entity.Permission;
import com.github.zxs1994.java_template.mapper.PermissionMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Component
@Profile("dev")
public class PermissionScanner implements ApplicationRunner {

    @Value("${permission.scan-on-startup:false}")
    private boolean scanOnStartup;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private SecurityProperties securityProperties;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public void run(ApplicationArguments args) {

        if (!scanOnStartup) return;

        System.out.println("=== PermissionScanner：开始扫描权限 ===");

        // 1️⃣ 扫描前统一标记为已删除
        permissionMapper.update(
                null,
                new UpdateWrapper<Permission>()
                        .ne("module", "ALL")
                        .set("deleted", 1)
        );

        List<String> permitUrls = securityProperties.getPermitUrls();

        // 0️⃣ 全局权限（只插一次）
        initGlobalPermissions();

        Map<String, Object> controllers =
                applicationContext.getBeansWithAnnotation(RestController.class);

        for (Object controllerBean : controllers.values()) {

            Class<?> clazz = AopUtils.getTargetClass(controllerBean);

            // 排除 swagger
            String className = clazz.getName().toLowerCase();
            if (className.contains("swagger") || className.contains("openapi")) {
                continue;
            }

            // 类级别 path
            String basePath = "";
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping rm = clazz.getAnnotation(RequestMapping.class);
                if (rm.value().length > 0) basePath = rm.value()[0];
            }

            // module（稳定）
            String module = clazz.getSimpleName()
                    .replace("Controller", "")
                    .toLowerCase();

            // moduleName（展示）
            String moduleName = module;
            if (clazz.isAnnotationPresent(Tag.class)) {
                Tag tag = clazz.getAnnotation(Tag.class);
                if (!tag.name().isEmpty()) moduleName = tag.name();
            }

            // 1️⃣ 模块总开关（只插一次）
            initModulePermission(module, moduleName);

            // 2️⃣ 接口级权限
            for (Method method : clazz.getDeclaredMethods()) {

                MappingInfo mapping = resolveMapping(method);
                if (mapping == null) continue;

                for (String path : mapping.paths) {

                    String fullPath = ("/" + basePath + "/" + path)
                            .replaceAll("/+", "/");
                    if (!"/".equals(fullPath) && fullPath.endsWith("/")) {
                        fullPath = fullPath.substring(0, fullPath.length() - 1);
                    }

                    if (fullPath.startsWith("/v3/api-docs")
                            || fullPath.startsWith("/swagger-ui")) continue;

                    /* ---------- auth_level 判断 ---------- */

                    int authLevel = 0; // 默认权限校验

                    // authLevel 后期增加什么类型应该也从配置文件中读取
                    for (String permit : permitUrls) {
                        if (pathMatcher.match(permit, fullPath)) {
                            authLevel = 1; // 白名单
                            break;
                        }
                    }

                    String code = module + ":" + method.getName().toLowerCase();

                    String name = code;
                    if (method.isAnnotationPresent(Operation.class)) {
                        Operation op = method.getAnnotation(Operation.class);
                        if (!op.summary().isEmpty()) name = op.summary();
                    }

                    Permission existing = permissionMapper.selectAny(
                            code,
                            mapping.method,
                            fullPath
                    );

                    if (existing != null) {
                        existing.setName(name);
                        existing.setModule(module);
                        existing.setModuleName(moduleName);
                        existing.setAuthLevel(authLevel);
                        existing.setDeleted(false);
                        permissionMapper.restoreByUnique(existing.getId(), name, module, moduleName, authLevel);
                    } else {
                        Permission p = new Permission();
                        p.setName(name);
                        p.setCode(code);
                        p.setMethod(mapping.method);
                        p.setPath(fullPath);
                        p.setModule(module);
                        p.setModuleName(moduleName);
                        p.setAuthLevel(authLevel);
                        permissionMapper.insert(p);
                    }
                }
            }
        }

        System.out.println("=== PermissionScanner：权限扫描完成 ===");
    }

    /* -------------------- 辅助方法 -------------------- */

    private void initGlobalPermissions() {
        insertIfAbsent("全局模块", "ALL", "*", "*", "ALL", "全局");
        insertIfAbsent("全局查看", "ALL_GET", "GET", "*", "ALL", "全局");
        insertIfAbsent("全局创建", "ALL_POST", "POST", "*", "ALL", "全局");
        insertIfAbsent("全局修改", "ALL_PUT", "PUT", "*", "ALL", "全局");
        insertIfAbsent("全局删除", "ALL_DELETE", "DELETE", "*", "ALL", "全局");
    }

    private void initModulePermission(String module, String moduleName) {
        insertIfAbsent(
                moduleName + "模块",
                "MODULE_" + module.toUpperCase(),
                "*",
                "/" + module + "/**",
                module,
                moduleName
        );
    }

    private void insertIfAbsent(String name, String code,
                                String method, String path,
                                String module, String moduleName) {

        Permission existing = permissionMapper.selectAny(
                code,
                method,
                path
        );

        if (existing != null) {
            permissionMapper.restoreByUnique(existing.getId(), name, module, moduleName, 0);
            return;
        };

        Permission p = new Permission();
        p.setName(name);
        p.setCode(code);
        p.setMethod(method);
        p.setPath(path);
        p.setModule(module);
        p.setModuleName(moduleName);
        permissionMapper.insert(p);
    }

    private MappingInfo resolveMapping(Method method) {

        if (method.isAnnotationPresent(GetMapping.class)) {
            return new MappingInfo("GET", method.getAnnotation(GetMapping.class).value());
        }
        if (method.isAnnotationPresent(PostMapping.class)) {
            return new MappingInfo("POST", method.getAnnotation(PostMapping.class).value());
        }
        if (method.isAnnotationPresent(PutMapping.class)) {
            return new MappingInfo("PUT", method.getAnnotation(PutMapping.class).value());
        }
        if (method.isAnnotationPresent(DeleteMapping.class)) {
            return new MappingInfo("DELETE", method.getAnnotation(DeleteMapping.class).value());
        }
        if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping rm = method.getAnnotation(RequestMapping.class);
            String m = rm.method().length > 0 ? rm.method()[0].name() : "GET";
            return new MappingInfo(m, rm.value());
        }
        return null;
    }

    static class MappingInfo {
        String method;
        String[] paths;

        MappingInfo(String method, String[] paths) {
            this.method = method;
            this.paths = (paths == null || paths.length == 0)
                    ? new String[]{""}
                    : paths;
        }
    }
}