package devtools;

import java.util.Map;
import java.util.Set;

public class GeneratorConfig {
    /**
     * 不生成控制器的表
     */
    public static final Set<String> noControllerTables = Set.of(
            "sys__user_role",
            "sys__role_permission"
    );

    /**
     * 使用自增ID的表
     */
    public static final Set<String> autoIdTables = Set.of(
            "sys__permission",
            "sys__user_role",
            "sys__role_permission"
    );

    /**
     * 只读字段
     */
    public static final Set<String> readOnlyFields = Set.of(
            "id",
            "source",
            "token_version"
    );

    /**
     * 查询字段配置（queryConfig）
     *
     * 该 Map 用于动态生成查询逻辑，每个字段可以指定其搜索操作符。
     *
     * key   : 实体类字段名（或数据库列名，视代码生成策略而定）
     * value : Map<String, String>，用于配置字段的附加信息：
     *      "operator" : 搜索操作符，可选值及说明：
     *          - "eq"       精确匹配（=）
     *          - "like"     模糊匹配（SQL LIKE '%value%'）
     *          - "in"       集合匹配（SQL IN (...)）
     *          - "between"  区间匹配（需配合 startField/endField 使用）
     *          - "gt"       大于（>）
     *          - "lt"       小于（<）
     *          - "ge"       大于等于（>=）
     *          - "le"       小于等于（<=）
     *          - "ne"       不等于（<>）
     *
     * 使用示例：
     *  - name 字段使用 like 查询
     *  - status 字段使用 eq 精确匹配
     *
     * 该配置通常在 Service 层或者生成的 Query 类中使用，结合 QueryWrapper 自动构建查询条件。
     */
    public static final Map<String, Map<String,String>> queryConfig = Map.of(
            "name", Map.of("operator","like"),
            "status", Map.of("operator","eq")
    );

}
