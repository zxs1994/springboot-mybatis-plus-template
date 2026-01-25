package ${package.Query};

import com.github.zxs1994.java_template.common.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ${entity}Query extends BaseQuery {

<#-- 循环表字段 -->
<#list table.fields as field>
    <#-- 只生成在 queryConfig 中配置过的字段 -->
    <#if queryConfig[field.name]?exists>
        @Schema(description="${field.comment!field.name}")
        private ${field.propertyType} ${field.propertyName};
    </#if>

</#list>

}