package com.github.zxs1994.java_template.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import com.github.zxs1994.java_template.common.BaseEntity;

/**
 * <p>
 * 用户表 实体
 * </p>
 *
 * @author xusheng
 * @since 2026-01-07 16:59:59
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user")
@Schema(description = "用户表")
public class User extends BaseEntity {

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "用户名")
    private String name;

    @Schema(description = "密码")
    private String password;

}