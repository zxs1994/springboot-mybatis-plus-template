package com.github.zxs1994.java_template.dto;

import com.github.zxs1994.java_template.entity.SysDept;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TenantDto extends SysDept {
     private SysUserDto adminUser;
}
