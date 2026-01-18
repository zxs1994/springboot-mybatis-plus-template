package com.github.zxs1994.java_template.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.zxs1994.java_template.entity.SysRole;
import com.github.zxs1994.java_template.entity.SysUserRole;
import com.github.zxs1994.java_template.mapper.SysRoleMapper;
import com.github.zxs1994.java_template.mapper.SysUserRoleMapper;
import com.github.zxs1994.java_template.service.ISysUserRoleService;
import com.github.zxs1994.java_template.common.SystemProtectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统--用户-角色关联表 服务实现类
 * </p>
 *
 * @author xusheng
 * @since 2026-01-10 01:41:52
 */
@Service
@RequiredArgsConstructor
public class SysUserRoleServiceImpl extends SystemProtectService<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysRoleMapper sysRoleMapper;

    @Override
    public Map<Long, List<SysRole>> getRolesByUserIds(List<Long> userIds) {
        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(
                new LambdaQueryWrapper<SysUserRole>().in(SysUserRole::getUserId, userIds)
        );

        List<Long> roleIds = userRoles.stream()
                .map(SysUserRole::getRoleId)
                .distinct()
                .collect(Collectors.toList());

        List<SysRole> roles = sysRoleMapper.selectBatchIds(roleIds);

        Map<Long, List<SysRole>> map = new HashMap<>();
        for (SysUserRole ur : userRoles) {
            SysRole role = roles.stream()
                    .filter(r -> r.getId().equals(ur.getRoleId()))
                    .findFirst().orElse(null);
            map.computeIfAbsent(ur.getUserId(), k -> new ArrayList<>());
            if (role != null) {
                map.get(ur.getUserId()).add(role);
            }
        }
        return map;
    }
}
