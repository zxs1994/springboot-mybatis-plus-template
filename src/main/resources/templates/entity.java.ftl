package ${package.Entity};

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import ${basePackage}.common.BaseEntity;

/**
 * <p>
 * ${table.comment} 实体
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
    <#-- 自动忽略敏感字段 -->
    <#if field.name == "token_version">
    @JsonIgnore
    </#if>
    <#-- 密码类字段：只写 -->
    <#if field.name == "password">
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    </#if>
    <#-- source字段：只读 -->
    <#if field.name == "source">
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    </#if>
    <#-- 逻辑删除 -->
    <#if field.logicDeleteField>
    @TableLogic
    @JsonIgnore
    </#if>
    @Schema(description = "${field.comment}")
    <#-- 自动填充 -->
    <#if field.fill??>
    @TableField(fill = FieldFill.${field.fill})
    <#elseif field.name != field.columnName>
    @TableField("${field.columnName}")
    </#if>
    private ${field.propertyType} ${field.propertyName};

</#list>
}