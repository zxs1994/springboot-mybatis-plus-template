package com.github.zxs1994.java_template.service;

import com.github.zxs1994.java_template.dto.TenantDto;
import com.github.zxs1994.java_template.entity.SysDept;
import com.github.zxs1994.java_template.vo.LoginVo;

import java.util.List;

public interface IPlatformTenantService {
    /**
     * 新增租户和默认管理员
     *
     * @param dto 租户信息和管理员信息
     * @return 新增租户ID
     */
    Long addTenantWithAdmin(TenantDto dto);

    List<SysDept> list();

    LoginVo switchTo(Long tenantId);
}
