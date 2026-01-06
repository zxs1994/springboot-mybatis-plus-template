package com.github.zxs1994.java_template.service.impl;

import com.github.zxs1994.java_template.entity.UserRole;
import com.github.zxs1994.java_template.mapper.UserRoleMapper;
import com.github.zxs1994.java_template.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户-角色关联表 服务实现类
 * </p>
 *
 * @author xusheng
 * @since 2026-01-04 17:32:23
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}
