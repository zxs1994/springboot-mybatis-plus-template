package com.github.zxs1994.java_template.controller;

import com.github.zxs1994.java_template.common.BizException;
import com.github.zxs1994.java_template.entity.RolePermission;
import com.github.zxs1994.java_template.service.IRolePermissionService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 角色-权限关联表 Controller 控制器
 * </p>
 *
 * @author xusheng
 * @since 2026-01-09 12:29:54
 */

@RestController
@RequestMapping("/rolePermission")
@Tag(name = "角色-权限关联", description = "角色-权限关联控制器")
public class RolePermissionController {

    @Autowired
    private IRolePermissionService rolePermissionService;

    @GetMapping
    @Operation(summary = "角色-权限关联列表")
    public List<RolePermission> list() {
        return rolePermissionService.list();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取角色-权限关联")
    public RolePermission get(@PathVariable Long id) {
        RolePermission rolePermission = rolePermissionService.getById(id);
        if (rolePermission == null) {
            throw new BizException(404, "角色-权限关联未找到");
        }
        return rolePermission;
    }

    @PostMapping
    @Operation(summary = "新增角色-权限关联")
    public RolePermission save(@RequestBody RolePermission rolePermission) {
        boolean success = rolePermissionService.save(rolePermission);
        if (!success) {
            throw new BizException(400, "新增角色-权限关联失败");
        }
        return rolePermission;
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新角色-权限关联")
    public RolePermission update(@PathVariable Long id, @RequestBody RolePermission rolePermission) {
        rolePermission.setId(id);
        boolean success = rolePermissionService.updateById(rolePermission);
        if (!success) {
            throw new BizException(400, "更新角色-权限关联失败");
        }
        return rolePermission;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色-权限关联")
    public Void delete(@PathVariable Long id) {
        boolean success = rolePermissionService.removeById(id);
        if (!success) {
            throw new BizException(400, "删除角色-权限关联失败");
        }
        return null;
    }

    @GetMapping("/page")
    @Operation(summary = "角色-权限关联列表(分页)")
    public Page<RolePermission> page(@RequestParam(defaultValue = "1") long page,
                                 @RequestParam(defaultValue = "10") long size) {
        return rolePermissionService.page(new Page<>(page, size));
    }
}