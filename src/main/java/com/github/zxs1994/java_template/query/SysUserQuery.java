package com.github.zxs1994.java_template.query;

import com.github.zxs1994.java_template.common.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;

public class SysUserQuery extends BaseQuery {

    @Schema(description="用户名称")
    private String name;

}
