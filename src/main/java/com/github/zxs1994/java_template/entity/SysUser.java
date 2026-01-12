package com.github.zxs1994.java_template.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import com.github.zxs1994.java_template.common.BaseEntity;

/**
 * <p>
 * 系统--用户表 实体
 * </p>
 *
 * @author xusheng
 * @since 2026-01-12 18:26:58
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys__user")
@Schema(description = "系统--用户表")
public class SysUser extends BaseEntity {

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "用户名")
    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "密码")
    private String password;

    @JsonIgnore
    @Schema(description = "token版本")
    private Integer tokenVersion;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "数据来源：SYSTEM=系统内置，USER=用户创建")
    private String source;

    @TableLogic
    @JsonIgnore
    @Schema(description = "逻辑删除")
    private Boolean deleted;

}