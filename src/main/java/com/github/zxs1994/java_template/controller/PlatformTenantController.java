package com.github.zxs1994.java_template.controller;

import com.github.zxs1994.java_template.config.security.LoginUser;
import com.github.zxs1994.java_template.dto.TenantDto;
import com.github.zxs1994.java_template.entity.SysDept;
import com.github.zxs1994.java_template.entity.SysUser;
import com.github.zxs1994.java_template.service.IPlatformTenantService;
import com.github.zxs1994.java_template.util.CurrentUser;
import com.github.zxs1994.java_template.vo.LoginVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 平台--租户接口
 * </p>
 *
 * @author xusheng
 * @since 2026-01-22
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/platform/tenant")
@Tag(name = "平台--租户", description = "平台--租户控制器")
public class PlatformTenantController {

    private final IPlatformTenantService platformTenantService;

    @PostMapping
    @Operation(summary = "新增租户")
    @Transactional
    public Long add(@RequestBody TenantDto dto) {
        return platformTenantService.addTenantWithAdmin(dto);
    }

    @GetMapping
    @Operation(summary = "获取租户列表")
    public List<SysDept> list() {
        return platformTenantService.list();
    }

    @PostMapping("/switch/{id}")
    @Operation(summary = "切换到目标租户")
    // 对外接口路径追求简洁，对内变量名追求明确
    public LoginVo switchTo(@PathVariable("id") Long tenantId) {
        return platformTenantService.switchTo(tenantId);
    }
}
