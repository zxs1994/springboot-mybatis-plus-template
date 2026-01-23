package devtools;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CodeGenerator {

    public static void main(String[] args) throws IOException {

        String url = LoadYaml.getDevProperty("spring", "datasource", "url");
        String username = LoadYaml.getDevProperty("spring", "datasource", "username");
        String password = LoadYaml.getDevProperty("spring", "datasource", "password");
        String basePackage = LoadYaml.getBasePackage();

//        System.out.println("数据库 URL: " + url);
//        System.out.println("数据库 用户名: " + username);
//        System.out.println("数据库 密码: " + password);
//        System.out.println("项目基础包: " + basePackage);

        String tableName = "sys__user";
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> builder
                        .author("xusheng")
                        .outputDir(System.getProperty("user.dir") + "/src/main/java")
                        .commentDate("yyyy-MM-dd HH:mm:ss")
                        .disableOpenDir() // 禁止自动打开输出目录
                )
                .packageConfig(builder ->
                    builder.parent(basePackage) // 父包名
//                            .entity("entity")
//                            .mapper("mapper")
//                            .service("service")
//                            .controller("controller")
//                            .serviceImpl("service.impl")
//                            .xml("mapper.xml")
                )
                .strategyConfig(builder -> builder
//                         .addInclude(tableName)

                        .entityBuilder()
                            .enableTableFieldAnnotation() // ✅ 强烈推荐
                            .addIgnoreColumns("created_at", "updated_at")
                            .logicDeleteColumnName("deleted")
                            .enableFileOverride() // 覆盖生成的文件

                )
                .templateEngine(new FreemarkerTemplateEngine())
                .injectionConfig(builder -> {
                    Map<String, Object> customMap = new HashMap<>();

                    customMap.put("basePackage", basePackage);

                    // 只读字段（模板里统一处理）
                    customMap.put("readOnlyFields", Set.of(
                            "id",
                            "source",
                            "token_version"
                    ));

                    // 使用自增ID的表（模板里统一处理）
                    customMap.put("autoIdTables", Set.of(
                            "sys__permission",
                            "sys__user_role",
                            "sys__role_permission"
                    ));

                    builder.customMap(customMap);
                })
                .execute();

    }
}

