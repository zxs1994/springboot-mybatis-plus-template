package com.github.zxs1994.java_template.service;

import com.github.zxs1994.java_template.dto.LoginRequest;
import com.github.zxs1994.java_template.dto.LoginResponse;
import com.github.zxs1994.java_template.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author xusheng
 * @since 2026-01-04 17:45:42
 */
public interface IUserService extends IService<User> {
    LoginResponse login(LoginRequest req);
}
