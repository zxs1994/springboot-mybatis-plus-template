package com.github.zxs1994.java_template.config;

import com.github.zxs1994.java_template.common.NoApiWrap;
import com.github.zxs1994.java_template.config.security.SecurityProperties;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springdoc.core.customizers.OpenApiCustomizer;

import java.util.LinkedHashMap;
import java.util.List;

@Configuration
public class OpenApiConfig {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private ProjectProperties projectProperties;

    /**
     * OpenAPI 全局配置 JWT 安全
     */
    @Bean
    public OpenAPI openAPI() {

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

//        System.out.println(projectProperties.getName());

        return new OpenAPI()
                .info(new Info()
                        .title(projectProperties.getName())
                        .version(projectProperties.getVersion())
                        .description(projectProperties.getDescription())
                )
                .components(new Components().addSecuritySchemes("jwt", securityScheme));
    }

    @Bean
    public GroupedOpenApi sysApi() {
        return GroupedOpenApi.builder()
                .group("系统管理")
                .pathsToMatch("/sys/**")
                .addOperationCustomizer(apiResponseCustomizer())
                .addOpenApiCustomizer(securityCustomizer())
                .build();
    }

    @Bean
    public GroupedOpenApi bizApi() {
        return GroupedOpenApi.builder()
                .group("业务接口")
                .pathsToExclude("/sys/**")
                .addOperationCustomizer(apiResponseCustomizer())
                .addOpenApiCustomizer(securityCustomizer())
                .build();
    }

    /**
     * 每个接口的 Operation 自定义，用于包装 200 响应为 ApiResponse<T>
     */
    private OperationCustomizer apiResponseCustomizer() {
        return (operation, handlerMethod) -> {
            boolean noWrap = handlerMethod.hasMethodAnnotation(NoApiWrap.class)
                    || handlerMethod.getBeanType().isAnnotationPresent(NoApiWrap.class);

            if (noWrap) return operation;

            Class<?> returnType = handlerMethod.getMethod().getReturnType();
            if (io.swagger.v3.oas.models.responses.ApiResponse.class
                    .isAssignableFrom(returnType)) {
                return operation;
            }

            io.swagger.v3.oas.models.responses.ApiResponse response200 =
                    operation.getResponses().get("200");

            if (response200 != null && response200.getContent() != null) {
                response200.getContent().forEach((mediaType, media) -> {
                    Schema<?> originalSchema = media.getSchema();
                    if (originalSchema == null) return;

                    Schema<?> wrapper = new ObjectSchema()
                            .addProperty("success", new BooleanSchema().example(true))
                            .addProperty("code", new IntegerSchema().example(200))
                            .addProperty("data", originalSchema)
                            .addProperty("msg", new StringSchema().example("ok"))
                            .addProperty("version", new StringSchema().example("1.0.0"));

                    media.setSchema(wrapper);
                });
            }
            return operation;
        };
    }

    /**
     * 用于控制每个接口右边有没有🔒, 不在白名单的都加锁
     */
    private OpenApiCustomizer securityCustomizer() {
        List<String> permitUrls = securityProperties.getPermitUrls();
        AntPathMatcher matcher = new AntPathMatcher();

        return openApi -> openApi.getPaths().forEach((path, pathItem) -> {
            boolean isPermit = permitUrls.stream()
                    .anyMatch(pattern -> matcher.match(pattern, path));

            if (!isPermit) {
                pathItem.readOperations().forEach(op ->
                        op.addSecurityItem(new SecurityRequirement().addList("jwt"))
                );
            }
        });
    }
}