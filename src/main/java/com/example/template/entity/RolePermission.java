package com.example.template.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import com.example.template.common.BaseEntity;

/**
 * <p>
 * RolePermission 实体
 * </p>
 *
 * @author xusheng
 * @since 2026-01-04 17:53:21
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("role_permission")
@Schema(description = "角色-权限关联表")
public class RolePermission extends BaseEntity {

    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "权限ID")
    private Long permissionId;

}