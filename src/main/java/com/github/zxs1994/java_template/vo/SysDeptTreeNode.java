package com.github.zxs1994.java_template.vo;

import com.github.zxs1994.java_template.entity.SysDept;
import com.github.zxs1994.java_template.entity.SysUser;
import com.github.zxs1994.java_template.util.TreeUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysDeptTreeNode extends SysDeptVo implements TreeUtils.TreeNode, TreeUtils.HasChildren<SysDeptTreeNode> {

    @Schema(description = "🌿树枝", example = "[]")
    private List<SysDeptTreeNode> children = new ArrayList<>();

    @Schema(description = "👤部门用户列表", example = "[]")
    private List<SysUser> users = new ArrayList<>();

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public Long getParentId() {
        return super.getParentId();
    }
}
