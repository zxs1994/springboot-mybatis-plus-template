package com.github.zxs1994.java_template.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import com.github.zxs1994.java_template.common.BaseEntity;

/**
 * <p>
 * 权限表 实体
 * </p>
 *
 * @author xusheng
 * @since 2026-01-09 16:05:09
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("permission")
@Schema(description = "权限表")
public class Permission extends BaseEntity {

    @Schema(description = "权限名")
    private String name;

    @Schema(description = "权限编码")
    private String code;

    @Schema(description = "请求方式")
    private String method;

    @Schema(description = "接口路径")
    private String path;

    @Schema(description = "权限模块")
    private String module;

    @Schema(description = "权限模块名称")
    private String moduleName;

    @Schema(description = "访问级别：0权限校验 1白名单")
    private Integer authLevel;

    @TableLogic
    @JsonIgnore
    @Schema(hidden = true)
    private Boolean deleted;

}