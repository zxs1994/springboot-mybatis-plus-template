package com.github.zxs1994.java_template.vo;

import com.github.zxs1994.java_template.common.BaseEntity;
import com.github.zxs1994.java_template.dto.SysRoleDto;
import com.github.zxs1994.java_template.entity.SysPermission;
import com.github.zxs1994.java_template.entity.SysRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysRoleVo extends SysRoleDto {

    @Schema(description = "权限 list")
    List<SysPermission> permissions;

    public SysRoleVo(SysRole sysRole, List<SysPermission> permissions) {
        // 拷贝父类属性
        BeanUtils.copyProperties(sysRole, this);
        this.permissions = permissions;
        this.setPermissionIds(permissions.stream().map(BaseEntity::getId).toList());
    }
}
