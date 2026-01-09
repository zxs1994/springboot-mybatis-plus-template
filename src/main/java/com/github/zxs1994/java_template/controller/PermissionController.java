package com.github.zxs1994.java_template.controller;

import com.github.zxs1994.java_template.common.BizException;
import com.github.zxs1994.java_template.entity.Permission;
import com.github.zxs1994.java_template.service.IPermissionService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 权限表 Controller 控制器
 * </p>
 *
 * @author xusheng
 * @since 2026-01-09 12:29:54
 */

@RestController
@RequestMapping("/permission")
@Tag(name = "权限", description = "权限控制器")
public class PermissionController {

    @Autowired
    private IPermissionService permissionService;

    @GetMapping
    @Operation(summary = "权限列表")
    public List<Permission> list() {
        return permissionService.list();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取权限")
    public Permission get(@PathVariable Long id) {
        Permission permission = permissionService.getById(id);
        if (permission == null) {
            throw new BizException(404, "权限未找到");
        }
        return permission;
    }

    @PostMapping
    @Operation(summary = "新增权限")
    public Permission save(@RequestBody Permission permission) {
        boolean success = permissionService.save(permission);
        if (!success) {
            throw new BizException(400, "新增权限失败");
        }
        return permission;
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新权限")
    public Permission update(@PathVariable Long id, @RequestBody Permission permission) {
        permission.setId(id);
        boolean success = permissionService.updateById(permission);
        if (!success) {
            throw new BizException(400, "更新权限失败");
        }
        return permission;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除权限")
    public Void delete(@PathVariable Long id) {
        boolean success = permissionService.removeById(id);
        if (!success) {
            throw new BizException(400, "删除权限失败");
        }
        return null;
    }

    @GetMapping("/page")
    @Operation(summary = "权限列表(分页)")
    public Page<Permission> page(@RequestParam(defaultValue = "1") long page,
                                 @RequestParam(defaultValue = "10") long size) {
        return permissionService.page(new Page<>(page, size));
    }
}