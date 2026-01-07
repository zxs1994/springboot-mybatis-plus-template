package com.github.zxs1994.java_template;

import com.github.zxs1994.java_template.common.ApiResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class Application {
    @Autowired
    private Environment env;

    @PostConstruct
    public void init() {
        ApiResponse.PROJECT_VERSION = env.getProperty("project.version");
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}