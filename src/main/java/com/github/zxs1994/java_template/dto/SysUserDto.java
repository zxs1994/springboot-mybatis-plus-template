package com.github.zxs1994.java_template.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.zxs1994.java_template.config.LongListToStringSerializer;
import com.github.zxs1994.java_template.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserDto extends SysUser {

    @Schema(description = "角色ids", example = "[8808]")
    @JsonSerialize(using = LongListToStringSerializer.class)
    List<Long> roleIds;
}
