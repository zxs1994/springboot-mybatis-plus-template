package com.github.zxs1994.java_template.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import com.github.zxs1994.java_template.common.BaseEntity;

/**
 * <p>
 * 权限表 实体
 * </p>
 *
 * @author xusheng
 * @since 2026-01-07 15:39:50
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("permission")
@Schema(description = "权限表")
public class Permission extends BaseEntity {

    @Schema(description = "权限名")
    private String name;

    @Schema(description = "权限编码")
    private String code;

}