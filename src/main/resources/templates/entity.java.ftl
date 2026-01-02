package ${package.Entity};

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import ${basePackage}.common.BaseEntity;

/**
 * <p>
 * ${entity} 实体
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("${table.name}")
@Schema(description = "${table.comment}")
public class ${entity} extends BaseEntity {

<#list table.fields as field>
    <#-- 主键 -->
    <#if field.keyFlag>
    @TableId(type = IdType.AUTO)
    </#if>
    <#-- 逻辑删除 -->
    <#if field.logicDeleteField>
    @TableLogic
    </#if>
    <#-- 自动填充 -->
    <#if field.fill??>
    @TableField(fill = FieldFill.${field.fill})
    <#elseif field.name != field.columnName>
    @TableField("${field.columnName}")
    </#if>
    @Schema(description = "${field.comment}")
    private ${field.propertyType} ${field.propertyName};

</#list>
}