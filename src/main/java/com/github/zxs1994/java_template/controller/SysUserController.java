package com.github.zxs1994.java_template.controller;

import com.github.zxs1994.java_template.common.BasePage;
import com.github.zxs1994.java_template.common.BizException;
import com.github.zxs1994.java_template.dto.SysUserDto;
import com.github.zxs1994.java_template.dto.UserInfoDto;
import com.github.zxs1994.java_template.entity.SysUser;
import com.github.zxs1994.java_template.service.ISysPermissionService;
import com.github.zxs1994.java_template.service.ISysUserService;
import com.github.zxs1994.java_template.util.CurrentUser;
import com.github.zxs1994.java_template.vo.SysUserInfoVo;
import com.github.zxs1994.java_template.vo.SysUserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 系统--用户表 Controller 控制器
 * </p>
 *
 * @author xusheng
 * @since 2026-01-10 01:41:52
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/sys/user")
@Tag(name = "系统--用户", description = "系统--用户控制器")
public class SysUserController {

    private final ISysUserService sysUserService;
    private final ISysPermissionService sysPermissionService;

    @GetMapping("/page")
    @Operation(summary = "用户列表(分页)")
    public Page<SysUserVo> page(BasePage query) {
        return sysUserService.page(query);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取用户")
    public SysUser getById(@PathVariable Long id) {
        SysUser sysUser = sysUserService.getById(id);
        if (sysUser == null) {
            throw new BizException(404, "用户未找到");
        }
        return sysUser;
    }

    @PostMapping
    @Operation(summary = "新增用户")
    public Long save(@RequestBody SysUserDto sysUserDto) {
        Long id = sysUserService.save(sysUserDto);
        if (id == null) {
            throw new BizException(400, "新增用户失败");
        }
        return id;
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新用户")
    public void updateById(@PathVariable Long id, @RequestBody SysUserDto sysUserDto) {
        sysUserDto.setId(id);
        boolean success = sysUserService.updateById(sysUserDto);
        if (!success) {
            throw new BizException(400, "更新用户失败");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    public void removeById(@PathVariable Long id) {
        boolean success = sysUserService.removeById(id);
        if (!success) {
            throw new BizException(400, "删除用户失败");
        }
    }

    @GetMapping
    @Operation(summary = "用户列表")
    public List<SysUser> list() {
        return sysUserService.list();
    }

    @GetMapping("/info")
    @Operation(summary = "获取登录用户信息")
    public SysUserInfoVo getUserInfo() {
        Long userId = CurrentUser.getId();
        SysUser sysUser = sysUserService.getById(userId);
        List<String> list = sysPermissionService.getCodesByUserId(userId);
        return new SysUserInfoVo(sysUser, list);
    }

    @PutMapping("/info")
    @Operation(summary = "修改登录用户信息")
    public void updateUserInfo(@RequestBody UserInfoDto userInfo) {
        Long userId = CurrentUser.getId();
        SysUser sysUser = new SysUser();
        sysUser.setId(userId);
        sysUser.setName(userInfo.getName());
        boolean success = sysUserService.updateById(sysUser);
        if (!success) {
            throw new BizException(400, "更新用户失败");
        }
    }

}