package com.github.zxs1994.java_template.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.zxs1994.java_template.common.BaseQuery;
import com.github.zxs1994.java_template.dto.LoginDto;
import com.github.zxs1994.java_template.dto.SysUserDto;
import com.github.zxs1994.java_template.vo.LoginVo;
import com.github.zxs1994.java_template.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.zxs1994.java_template.vo.SysUserVo;

/**
 * <p>
 * 系统--用户表 服务类
 * </p>
 *
 * @author xusheng
 * @since 2026-01-10 01:41:52
 */
public interface ISysUserService extends IService<SysUser> {
    LoginVo login(LoginDto req);

    void logout();

    Long save(SysUserDto sysUser);

    boolean updateById(SysUserDto sysUserDto);

    Page<SysUserVo> page(BaseQuery query);

    boolean removeById(Long id);

    SysUser getById(Long id);
}
