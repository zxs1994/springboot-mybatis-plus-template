package com.github.zxs1994.java_template.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.zxs1994.java_template.common.BasePage;
import com.github.zxs1994.java_template.common.BizException;
import com.github.zxs1994.java_template.dto.LoginDto;
import com.github.zxs1994.java_template.dto.SysUserDto;
import com.github.zxs1994.java_template.entity.SysRole;
import com.github.zxs1994.java_template.entity.SysUserRole;
import com.github.zxs1994.java_template.service.ISysUserRoleService;
import com.github.zxs1994.java_template.util.CurrentUser;
import com.github.zxs1994.java_template.vo.LoginVo;
import com.github.zxs1994.java_template.entity.SysUser;
import com.github.zxs1994.java_template.mapper.SysUserMapper;
import com.github.zxs1994.java_template.service.ISysUserService;
import com.github.zxs1994.java_template.config.security.jwt.JwtUtils;
import com.github.zxs1994.java_template.common.SystemProtectService;
import com.github.zxs1994.java_template.vo.SysUserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统--用户表 服务实现类
 * </p>
 *
 * @author xusheng
 * @since 2026-01-10 01:41:52
 */
@RequiredArgsConstructor
@Service
public class SysUserServiceImpl extends SystemProtectService<SysUserMapper, SysUser> implements ISysUserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final ISysUserRoleService sysUserRoleService;

    @Override
    public void logout() {
        Long userId = CurrentUser.getId();
        increaseTokenVersion(userId);
    }

    @Override
    public LoginVo login(LoginDto req) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("email", req.getEmail());
        SysUser sysUser = getOne(wrapper, false);
        if(sysUser == null || !passwordEncoder.matches(req.getPassword(), sysUser.getPassword())) {
            // 登录失败，抛业务异常
            throw new BizException(400, "邮箱或密码错误");
        }
        // 登录成功后

        // 1️⃣ tokenVersion 自增（数据库）
        increaseTokenVersion(sysUser.getId());
        // 2️⃣ 内存同步
        sysUser.setTokenVersion(sysUser.getTokenVersion() + 1);
        // 3️⃣ 生成 token
        String token = jwtUtils.generateToken(sysUser);

        LoginVo res = new LoginVo();
        res.setToken(token);
        return res;
    }

    @Transactional
    @Override
    public Long save(SysUserDto dto) {

        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(dto, sysUser);

        validateEmail(sysUser.getEmail());
        checkEmailDuplicate(sysUser.getEmail(), null);
        validatePassword(sysUser.getPassword());

        sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));

        super.save(sysUser);

        Long userId = sysUser.getId();
        List<Long> roleIds = dto.getRoleIds();

        saveUserRoles(userId, roleIds);

        return userId;
    }

    @Transactional
    public boolean updateById(SysUserDto dto) {
        Long userId = dto.getId();

        // 1️⃣ 更新用户基本信息
        SysUser user = new SysUser();
        BeanUtils.copyProperties(dto, user);
        updateById(user);

        // 2️⃣ 删除旧角色关联
        sysUserRoleService.remove(
                new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId, userId)
        );

        // 3️⃣ 新增新角色关联
        List<Long> roleIds = dto.getRoleIds();
        saveUserRoles(userId, roleIds);

        return true;
    }

    @Override
    public boolean updateById(SysUser sysUser) {
        String email = sysUser.getEmail();

        // 1️⃣ 校验 email
        if (email != null) {
            validateEmail(sysUser.getEmail());
            checkEmailDuplicate(email, sysUser.getId());
        }

        // 如果密码不为空，才加密更新
        if (sysUser.getPassword() != null && !sysUser.getPassword().isBlank()) {
            validatePassword(sysUser.getPassword());
            sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        } else {
            sysUser.setPassword(null); // 保持原密码
        }

        return super.updateById(sysUser);
    }

    @Override
    public Page<SysUserVo> page(BasePage query){
        // 1️⃣ 查询用户分页
        Page<SysUser> userPage = super.page(new Page<>(query.getPage(), query.getSize()),
                new QueryWrapper<SysUser>().like(StringUtils.hasText(query.getName()), "name", query.getName()));

        List<Long> userIds = userPage.getRecords().stream()
                .map(SysUser::getId)
                .collect(Collectors.toList());

        // 2️⃣ 查询用户角色（批量）
        Map<Long, List<SysRole>> userRolesMap;
        if (!userIds.isEmpty()) {
            userRolesMap = sysUserRoleService.getRolesByUserIds(userIds);
        } else {
            userRolesMap = new HashMap<>();
        }

        // 3️⃣ 组装 DTO
        List<SysUserVo> voList = userPage.getRecords().stream().map(u -> {
            SysUserVo vo = new SysUserVo();
            BeanUtils.copyProperties(u, vo);

            // 设置角色对象列表
            vo.setRoles(userRolesMap.getOrDefault(u.getId(), Collections.emptyList()));

            // 设置角色 ID 列表
            vo.setRoleIds(
                    vo.getRoles().stream()
                            .map(SysRole::getId)
                            .collect(Collectors.toList())
            );

            return vo;
        }).collect(Collectors.toList());

        // 4️⃣ 返回分页
        Page<SysUserVo> voPage = new Page<>();
        BeanUtils.copyProperties(userPage, voPage, "records");
        voPage.setRecords(voList);

        return voPage;
    }


    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new BizException(400, "邮箱不能为空");
        }
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new BizException(400, "邮箱格式不正确");
        }
    }

    private void checkEmailDuplicate(String email, Long excludeId) {
        boolean exists = this.lambdaQuery()
                .eq(SysUser::getEmail, email)
                .ne(excludeId != null, SysUser::getId, excludeId)
                .exists();
        if (exists) {
            throw new BizException(400, "该邮箱已被注册");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new BizException(400, "密码不能为空");
        }
        if (password.length() < 6) {
            throw new BizException(400, "密码长度不能少于6位");
        }
    }

    public void increaseTokenVersion(Long userId) {
        baseMapper.update(
                null,
                new UpdateWrapper<SysUser>()
                        .eq("id", userId)
                        .setSql("token_version = token_version + 1")
        );
    }

    /**
     * 给用户批量更新角色关联
     *
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     */
    private void saveUserRoles(Long userId, List<Long> roleIds) {
        if (roleIds != null && !roleIds.isEmpty()) {
            List<SysUserRole> list = roleIds.stream()
                    .map(roleId -> {
                        SysUserRole sysUserRole = new SysUserRole();
                        sysUserRole.setUserId(userId);
                        sysUserRole.setRoleId(roleId);
                        return sysUserRole;
                    })
                    .collect(Collectors.toList());
            sysUserRoleService.saveBatch(list);
        }
    }
}
