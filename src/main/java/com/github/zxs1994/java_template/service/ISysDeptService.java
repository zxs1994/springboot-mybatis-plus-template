package com.github.zxs1994.java_template.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.zxs1994.java_template.common.BaseQuery;
import com.github.zxs1994.java_template.entity.SysDept;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.zxs1994.java_template.vo.SysDeptTreeNode;

import java.util.List;

/**
 * <p>
 * 系统--组织部门表 服务类
 * </p>
 *
 * @author xusheng
 * @since 2026-01-20 14:06:31
 */
public interface ISysDeptService extends IService<SysDept> {

    boolean removeById(Long id);

    boolean save(SysDept sysDept);

    List<SysDeptTreeNode> getTree();

    Page<SysDept> page(BaseQuery query);
}
