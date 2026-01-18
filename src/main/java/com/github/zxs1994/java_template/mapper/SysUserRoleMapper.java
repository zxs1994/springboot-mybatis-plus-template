package com.github.zxs1994.java_template.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.zxs1994.java_template.entity.SysUserRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 系统--用户-角色关联表 Mapper 接口
 * </p>
 *
 * @author xusheng
 * @since 2026-01-10 01:41:52
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    @Select("select role_id from sys__user_role where user_id = #{userId}")
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);
}
