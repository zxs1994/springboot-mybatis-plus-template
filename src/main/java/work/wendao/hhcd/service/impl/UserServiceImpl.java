package work.wendao.hhcd.service.impl;

import work.wendao.hhcd.entity.User;
import work.wendao.hhcd.mapper.UserMapper;
import work.wendao.hhcd.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xusheng
 * @since 2026-01-02
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
