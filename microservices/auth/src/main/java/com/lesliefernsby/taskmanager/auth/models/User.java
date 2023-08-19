package com.lesliefernsby.taskmanager.auth.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private List<Role> roles;

    public User(String username, String password, List<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }
}
