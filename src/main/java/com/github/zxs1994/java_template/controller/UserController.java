package com.github.zxs1994.java_template.controller;

import com.github.zxs1994.java_template.common.BizException;
import com.github.zxs1994.java_template.entity.User;
import com.github.zxs1994.java_template.service.IUserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 用户表 Controller 控制器
 * </p>
 *
 * @author xusheng
 * @since 2026-01-04 17:53:21
 */

@RestController
@RequestMapping("/user")
@Tag(name = "用户", description = "用户控制器")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping
    @Operation(summary = "用户列表")
    public List<User> list() {
        return userService.list();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取用户")
    public User get(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            throw new BizException(404, "用户未找到");
        }
        return user;
    }

    @PostMapping
    @Operation(summary = "新增用户")
    public User save(@RequestBody User user) {
        boolean success = userService.save(user);
        if (!success) {
            throw new BizException(400, "新增用户失败");
        }
        return user;
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新用户")
    public User update(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        user.setPassword(null);
        boolean success = userService.updateById(user);
        if (!success) {
            throw new BizException(400, "更新用户失败");
        }
        return user;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    public Void delete(@PathVariable Long id) {
        boolean success = userService.removeById(id);
        if (!success) {
            throw new BizException(400, "删除用户失败");
        }
        return null;
    }

    @GetMapping("/page")
    @Operation(summary = "用户列表(分页)")
    public Page<User> page(@RequestParam(defaultValue = "1") long page,
                                   @RequestParam(defaultValue = "10") long size) {
        return userService.page(new Page<>(page, size));
    }

//    @PostMapping("/register")
//    @Operation(summary = "前端新增用户")
//    public User register(@RequestBody User user) {
//        return save(user);
//    }

}