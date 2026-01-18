package com.github.zxs1994.java_template.controller;

import com.github.zxs1994.java_template.common.BasePage;
import com.github.zxs1994.java_template.common.BizException;
import com.github.zxs1994.java_template.dto.SysRoleDto;
import com.github.zxs1994.java_template.entity.SysRole;
import com.github.zxs1994.java_template.service.ISysRoleService;
import com.github.zxs1994.java_template.vo.SysRoleVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 系统--角色表 Controller 控制器
 * </p>
 *
 * @author xusheng
 * @since 2026-01-10 01:41:52
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/sys/role")
@Tag(name = "系统--角色", description = "系统--角色控制器")
public class SysRoleController {

    private final ISysRoleService sysRoleService;

    @GetMapping("/page")
    @Operation(summary = "角色列表(分页)")
    public Page<SysRoleVo> page(BasePage query) {
        return sysRoleService.page(query);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取角色")
    public SysRole getById(@PathVariable Long id) {
        SysRole sysRole = sysRoleService.getById(id);
        if (sysRole == null) {
            throw new BizException(404, "角色未找到");
        }
        return sysRole;
    }

    @PostMapping
    @Operation(summary = "新增角色")
    public Long save(@RequestBody SysRoleDto dto) {

        Long id = sysRoleService.save(dto);
        if (id == null) {
            throw new BizException(400, "新增角色失败");
        }
        return id;
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新角色")
    public void updateById(@PathVariable Long id, @RequestBody SysRoleDto dto) {
        dto.setId(id);
        boolean success = sysRoleService.updateById(dto);
        if (!success) {
            throw new BizException(400, "更新角色失败");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色")
    public void removeById(@PathVariable Long id) {
        boolean success = sysRoleService.removeById(id);
        if (!success) {
            throw new BizException(400, "删除角色失败");
        }
    }

    @GetMapping
    @Operation(summary = "角色列表")
    public List<SysRole> list() {
        return sysRoleService.list();
    }

}