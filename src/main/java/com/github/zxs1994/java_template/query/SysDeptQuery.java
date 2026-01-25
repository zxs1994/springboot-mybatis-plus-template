package com.github.zxs1994.java_template.query;

import com.github.zxs1994.java_template.common.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysDeptQuery extends BaseQuery {

    @Schema(description="部门名称")
    private String name;

}