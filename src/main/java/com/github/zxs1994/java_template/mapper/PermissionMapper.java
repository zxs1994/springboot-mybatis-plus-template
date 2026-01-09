package com.github.zxs1994.java_template.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.zxs1994.java_template.entity.Permission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author xusheng
 * @since 2026-01-04 17:32:23
 */
public interface PermissionMapper extends BaseMapper<Permission> {
    /**
     * 查询用户拥有的权限
     * @param userId 用户ID
     * @return 权限列表
     */
    @Select("SELECT p.* " +
            "FROM permission p " +
            "JOIN role_permission rp ON rp.permission_id = p.id " +
            "JOIN user_role ur ON ur.role_id = rp.role_id " +
            "WHERE ur.user_id = #{userId} AND p.deleted = 0")
    List<Permission> selectByUserId(@Param("userId") Long userId);

    @Select("""
        SELECT *
        FROM permission
        WHERE code = #{code}
          AND method = #{method}
          AND path = #{path}
        LIMIT 1
    """)
    Permission selectAny(
            @Param("code") String code,
            @Param("method") String method,
            @Param("path") String path
    );

    @Update("""
        UPDATE permission
        SET deleted = 0,
            name = #{name},
            module = #{module},
            module_name = #{moduleName},
            auth_level = #{authLevel}
        WHERE id = #{id}
    """)
    void restoreByUnique(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("module") String module,
            @Param("moduleName") String moduleName,
            @Param("authLevel") int authLevel
    );
}
