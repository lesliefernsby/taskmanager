package com.lesliefernsby.taskmanager.auth.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.InputStream;


@Component
public class ConfigLoader {
    private AuthConfig authConfig;

    @PostConstruct
    public void loadConfig() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("auth-config.json")) {
            this.authConfig = objectMapper.readValue(is, AuthConfig.class);
        }
    }

    public AuthConfig getAuthConfig() {
        return authConfig;
    }
}

