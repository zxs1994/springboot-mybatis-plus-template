package devtools;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Properties;

public class CodeGenerator {

    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        try (InputStream in = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("application-dev.properties")) {
            if (in == null) {
                throw new RuntimeException("application.properties not found in classpath!");
            }
            props.load(in);
        }

        String url = props.getProperty("spring.datasource.url");
        String username = props.getProperty("spring.datasource.username");
        String password = props.getProperty("spring.datasource.password");
        String basePackage = "work.wendao.hhcd";
        String tableName = "user";
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
                        .addInclude(tableName)

                        .entityBuilder()
                            .entityBuilder()
                            .enableTableFieldAnnotation() // ✅ 强烈推荐
                            .addIgnoreColumns("id", "created_at", "updated_at", "deleted")
                            .enableFileOverride() // 覆盖生成的文件

                        .controllerBuilder()
                )
                .templateEngine(new FreemarkerTemplateEngine())
                .injectionConfig(builder -> builder
                        .customMap(Collections.singletonMap("basePackage", basePackage)) // 传给模板
                )
                .execute();

    }
}

