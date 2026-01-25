package ${package.Service}.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ${package.Entity}.${entity};
import ${package.Mapper}.${entity}Mapper;
import ${package.Service}.I${entity}Service;
import ${package.Query}.${entity}Query;
import ${package.Vo}.${entity}Vo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;

/**
 * <p>
 * ${table.comment!"${entity}"} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
public class ${entity}ServiceImpl extends ServiceImpl<${entity}Mapper, ${entity}> implements I${entity}Service {

    /**
     * 分页查询（返回 VO）
     */
    @Override
    public Page<${entity}Vo> page(${entity}Query query) {
        Page<${entity}> entityPage = new Page<>(query.getPage(), query.getSize());
        QueryWrapper<${entity}> qw = new QueryWrapper<>();

        <#-- 遍历 queryConfig，如果有 operator 配置，就生成对应条件 -->
        <#list queryConfig?keys as fieldName>
        <#assign operator = queryConfig[fieldName]["operator"]!"" >
        <#if operator??>
        if (query.get${fieldName?cap_first}() != null) {
            <#if operator == "eq">
            qw.eq("${fieldName}", query.get${fieldName?cap_first}());
            <#elseif operator == "like">
            if (StringUtils.hasText(query.get${fieldName?cap_first}())) {
                qw.like("${fieldName}", query.get${fieldName?cap_first}());
            }
            <#elseif operator == "in">
            qw.in("${fieldName}", query.get${fieldName?cap_first}());
            <#elseif operator == "gt">
            qw.gt("${fieldName}", query.get${fieldName?cap_first}());
            <#elseif operator == "lt">
            qw.lt("${fieldName}", query.get${fieldName?cap_first}());
            <#elseif operator == "between">
            qw.between("${fieldName}", query.get${fieldName?cap_first}Start(), query.get${fieldName?cap_first}End());
            </#if>
        }
        </#if>
        </#list>

        entityPage = super.page(entityPage, qw);
        return entityPageToVoPage(entityPage);
    }

    /**
     * 根据 ID 获取 VO
     */
    @Override
    public ${entity}Vo getVoById(Long id) {
        ${entity} entity = this.getById(id);
        if (entity == null) return null;
        return convertToVo(entity);
    }

    // ===================== 私有 VO 转换方法 =====================

    /**
     * 单个实体转 VO
     */
    private ${entity}Vo convertToVo(${entity} entity) {
        ${entity}Vo vo = new ${entity}Vo();
        BeanUtils.copyProperties(entity, vo);
        // TODO 组装额外数据
        return vo;
    }

    /**
     * 实体分页转 VO 分页
     */
    private Page<${entity}Vo> entityPageToVoPage(Page<${entity}> entityPage) {
        Page<${entity}Vo> voPage = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        voPage.setRecords(entityPage.getRecords().stream()
                .map(this::convertToVo)
                .toList());
        return voPage;
    }
}