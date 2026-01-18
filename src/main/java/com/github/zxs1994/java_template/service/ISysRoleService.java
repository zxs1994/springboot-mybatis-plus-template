package com.github.zxs1994.java_template.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.zxs1994.java_template.common.BasePage;
import com.github.zxs1994.java_template.dto.SysRoleDto;
import com.github.zxs1994.java_template.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.zxs1994.java_template.vo.SysRoleVo;

/**
 * <p>
 * 系统--角色表 服务类
 * </p>
 *
 * @author xusheng
 * @since 2026-01-10 01:41:52
 */
public interface ISysRoleService extends IService<SysRole> {

    Long save(SysRoleDto dto);

    Page<SysRoleVo> page(BasePage query);

    boolean updateById(SysRoleDto dto);
}
