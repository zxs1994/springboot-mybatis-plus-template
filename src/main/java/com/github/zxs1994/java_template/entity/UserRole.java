package com.github.zxs1994.java_template.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import com.github.zxs1994.java_template.common.BaseEntity;

/**
 * <p>
 * 用户-角色关联表 实体
 * </p>
 *
 * @author xusheng
 * @since 2026-01-09 16:05:09
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_role")
@Schema(description = "用户-角色关联表")
public class UserRole extends BaseEntity {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "角色ID")
    private Long roleId;

}