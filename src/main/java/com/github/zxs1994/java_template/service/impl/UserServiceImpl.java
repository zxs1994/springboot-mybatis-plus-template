package com.github.zxs1994.java_template.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.zxs1994.java_template.common.BizException;
import com.github.zxs1994.java_template.dto.LoginRequest;
import com.github.zxs1994.java_template.dto.LoginResponse;
import com.github.zxs1994.java_template.entity.User;
import com.github.zxs1994.java_template.mapper.UserMapper;
import com.github.zxs1994.java_template.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.zxs1994.java_template.config.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author xusheng
 * @since 2026-01-04 17:45:42
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserMapper userMapper;

    public LoginResponse login(LoginRequest req) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("email", req.getEmail());
        User user = getOne(wrapper, false);
        if(user == null || !passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            // 登录失败，抛业务异常
            throw new BizException(400, "用户名或密码错误");
        }
        // 登录成功后
        User newUser = new User();
        newUser.setId(user.getId());
        newUser.setTokenVersion(user.getTokenVersion() + 1);
        userMapper.updateById(newUser);

        String token = jwtUtils.generateToken(newUser);
        LoginResponse res = new LoginResponse();
        res.setToken(token);
        return res;
    }

    @Override
    public boolean save(User user) {
        // 1️⃣ 校验 email
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new BizException(400, "邮箱不能为空");
        }
        if (!user.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new BizException(400, "邮箱格式不正确");
        }

        // 2️⃣ 校验 email 是否重复
        boolean exists = this.lambdaQuery()
                .eq(User::getEmail, user.getEmail())
                .exists();

        if (exists) {
            throw new BizException(400, "该邮箱已被注册");
        }

        // 3️⃣ 校验密码
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new BizException(400, "密码不能为空");
        }
        if (user.getPassword().length() < 6) {
            throw new BizException(400, "密码长度不能少于6位");
        }

        // 4️⃣ 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 5️⃣ 保存
        return super.save(user);
    }
}
