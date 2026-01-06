package com.github.zxs1994.java_template.service.impl;

import com.github.zxs1994.java_template.entity.Permission;
import com.github.zxs1994.java_template.mapper.PermissionMapper;
import com.github.zxs1994.java_template.service.IPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author xusheng
 * @since 2026-01-04 17:32:23
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

}
