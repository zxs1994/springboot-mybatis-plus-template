package com.github.zxs1994.java_template.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
