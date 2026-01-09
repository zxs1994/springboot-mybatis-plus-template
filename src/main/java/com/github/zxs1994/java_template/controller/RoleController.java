package com.github.zxs1994.java_template.controller;

import com.github.zxs1994.java_template.common.BizException;
import com.github.zxs1994.java_template.entity.Role;
import com.github.zxs1994.java_template.service.IRoleService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 角色表 Controller 控制器
 * </p>
 *
 * @author xusheng
 * @since 2026-01-09 12:29:54
 */

@RestController
@RequestMapping("/role")
@Tag(name = "角色", description = "角色控制器")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @GetMapping
    @Operation(summary = "角色列表")
    public List<Role> list() {
        return roleService.list();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取角色")
    public Role get(@PathVariable Long id) {
        Role role = roleService.getById(id);
        if (role == null) {
            throw new BizException(404, "角色未找到");
        }
        return role;
    }

    @PostMapping
    @Operation(summary = "新增角色")
    public Role save(@RequestBody Role role) {
        boolean success = roleService.save(role);
        if (!success) {
            throw new BizException(400, "新增角色失败");
        }
        return role;
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新角色")
    public Role update(@PathVariable Long id, @RequestBody Role role) {
        role.setId(id);
        boolean success = roleService.updateById(role);
        if (!success) {
            throw new BizException(400, "更新角色失败");
        }
        return role;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色")
    public Void delete(@PathVariable Long id) {
        boolean success = roleService.removeById(id);
        if (!success) {
            throw new BizException(400, "删除角色失败");
        }
        return null;
    }

    @GetMapping("/page")
    @Operation(summary = "角色列表(分页)")
    public Page<Role> page(@RequestParam(defaultValue = "1") long page,
                                 @RequestParam(defaultValue = "10") long size) {
        return roleService.page(new Page<>(page, size));
    }
}