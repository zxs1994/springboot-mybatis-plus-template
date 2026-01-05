package com.example.template.util;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class LoadProperties {

    public static Properties load(String path) {
        Properties props = new Properties();
        try (Reader reader = Files.newBufferedReader(Path.of(path), StandardCharsets.UTF_8)) {
            props.load(reader); // 使用 Reader 而不是 InputStream
        } catch (IOException e) {
            throw new RuntimeException("加载 properties 文件失败: " + path, e);
        }
        return props;
    }

    public static String get(String path, String key) {
        return load(path).getProperty(key);
    }

    public static String getBasePackage() {
        return LoadProperties.get("src/main/resources/application.properties", "project.base-package");
    }
}