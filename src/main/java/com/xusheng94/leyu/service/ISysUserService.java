package com.xusheng94.leyu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xusheng94.leyu.dto.LoginDto;
import com.xusheng94.leyu.dto.SysUserDto;
import com.xusheng94.leyu.query.SysUserQuery;
import com.xusheng94.leyu.vo.LoginVo;
import com.xusheng94.leyu.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xusheng94.leyu.vo.SysUserVo;

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

    Page<SysUserVo> page(SysUserQuery query);

    boolean removeById(Long id);

    SysUser getById(Long id);
}
