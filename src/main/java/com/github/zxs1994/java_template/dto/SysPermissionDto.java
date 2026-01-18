package com.github.zxs1994.java_template.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SysPermissionDto {

    @Schema(description = "排序")
    private Integer sort;
}
