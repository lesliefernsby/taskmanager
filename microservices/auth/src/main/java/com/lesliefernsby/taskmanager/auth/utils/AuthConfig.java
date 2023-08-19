package com.lesliefernsby.taskmanager.auth.utils;

import lombok.Data;

@Data
public class AuthConfig {
    private String secretKey;
    private Long accessTokenValidity;
    private Long refreshTokenValidity;

}
