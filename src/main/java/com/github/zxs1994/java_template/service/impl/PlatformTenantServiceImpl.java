package com.github.zxs1994.java_template.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.zxs1994.java_template.config.security.LoginUser;
import com.github.zxs1994.java_template.config.security.jwt.JwtUtils;
import com.github.zxs1994.java_template.dto.SysUserDto;
import com.github.zxs1994.java_template.dto.TenantDto;
import com.github.zxs1994.java_template.entity.SysDept;
import com.github.zxs1994.java_template.entity.SysUser;
import com.github.zxs1994.java_template.enums.SourceType;
import com.github.zxs1994.java_template.mapper.SysDeptMapper;
import com.github.zxs1994.java_template.service.IPlatformTenantService;
import com.github.zxs1994.java_template.service.ISysDeptService;
import com.github.zxs1994.java_template.service.ISysUserService;
import com.github.zxs1994.java_template.util.CurrentUser;
import com.github.zxs1994.java_template.vo.LoginVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PlatformTenantServiceImpl implements IPlatformTenantService {

    private final JwtUtils jwtUtils;
    private final SysDeptMapper sysDeptMapper;
    private final ISysUserService sysUserService;
    private final ISysDeptService sysDeptService;

    @Override
    @Transactional
    public Long addTenantWithAdmin(TenantDto dto) {

        // 1️⃣ 生成雪花ID
        Long tenantId = IdWorker.getId();
        Long adminUserId = IdWorker.getId();

        // 2️⃣ 创建顶级租户（部门）
        SysDept tenant = new SysDept();
        tenant.setId(tenantId);
        tenant.setName(dto.getName());
        tenant.setParentId(null);               // 顶级部门
        tenant.setPath("/" + tenantId);
        tenant.setTenantId(tenantId);          // 顶级租户的 tenantId 是自己
        tenant.setLeaderId(adminUserId);
        sysDeptMapper.insert(tenant);

        // 3️⃣ 创建默认管理员
        SysUserDto admin = dto.getAdminUser();
        admin.setId(adminUserId);
        admin.setTenantId(tenantId);
        admin.setDeptId(tenantId);
        admin.setSource(SourceType.SYSTEM.getCode());    // 系统创建
        admin.setRoleIds(List.of(1L)); // 超级管理员
        sysUserService.save(admin);

        return tenantId;
    }

    @Override
    public List<SysDept> list() {
        // 只返回 parentId 为 null 的顶级部门（即企业/租户）
        return sysDeptService.list(
                Wrappers.<SysDept>lambdaQuery()
                        .isNull(SysDept::getParentId)
        );
    }

    @Override
    public LoginVo switchTo(Long tenantId) {

        SysUser sysUser = new SysUser();
        LoginUser loginUser = CurrentUser.getLoginUser();

        BeanUtils.copyProperties(Objects.requireNonNull(loginUser), sysUser);
        sysUser.setTenantId(tenantId);
        sysUser.setId(loginUser.getUserId());

        String token = jwtUtils.generateToken(sysUser);

        LoginVo res = new LoginVo();
        res.setToken(token);

        return res;
    }
}
