package com.xusheng94.leyu.dto;

import com.xusheng94.leyu.entity.SysDept;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TenantDto extends SysDept {
     private SysUserDto adminUser;
}
