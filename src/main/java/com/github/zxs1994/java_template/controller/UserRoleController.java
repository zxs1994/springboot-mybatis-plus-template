package com.github.zxs1994.java_template.controller;

import com.github.zxs1994.java_template.common.BizException;
import com.github.zxs1994.java_template.entity.UserRole;
import com.github.zxs1994.java_template.service.IUserRoleService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 用户-角色关联表 Controller 控制器
 * </p>
 *
 * @author xusheng
 * @since 2026-01-09 12:29:54
 */

@RestController
@RequestMapping("/userRole")
@Tag(name = "用户-角色关联", description = "用户-角色关联控制器")
public class UserRoleController {

    @Autowired
    private IUserRoleService userRoleService;

    @GetMapping
    @Operation(summary = "用户-角色关联列表")
    public List<UserRole> list() {
        return userRoleService.list();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取用户-角色关联")
    public UserRole get(@PathVariable Long id) {
        UserRole userRole = userRoleService.getById(id);
        if (userRole == null) {
            throw new BizException(404, "用户-角色关联未找到");
        }
        return userRole;
    }

    @PostMapping
    @Operation(summary = "新增用户-角色关联")
    public UserRole save(@RequestBody UserRole userRole) {
        boolean success = userRoleService.save(userRole);
        if (!success) {
            throw new BizException(400, "新增用户-角色关联失败");
        }
        return userRole;
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新用户-角色关联")
    public UserRole update(@PathVariable Long id, @RequestBody UserRole userRole) {
        userRole.setId(id);
        boolean success = userRoleService.updateById(userRole);
        if (!success) {
            throw new BizException(400, "更新用户-角色关联失败");
        }
        return userRole;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户-角色关联")
    public Void delete(@PathVariable Long id) {
        boolean success = userRoleService.removeById(id);
        if (!success) {
            throw new BizException(400, "删除用户-角色关联失败");
        }
        return null;
    }

    @GetMapping("/page")
    @Operation(summary = "用户-角色关联列表(分页)")
    public Page<UserRole> page(@RequestParam(defaultValue = "1") long page,
                                 @RequestParam(defaultValue = "10") long size) {
        return userRoleService.page(new Page<>(page, size));
    }
}