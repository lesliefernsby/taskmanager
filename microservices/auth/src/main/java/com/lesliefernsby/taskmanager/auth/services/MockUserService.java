package com.lesliefernsby.taskmanager.auth.services;

import com.lesliefernsby.taskmanager.auth.exceptions.AuthenticationException;
import com.lesliefernsby.taskmanager.auth.model.AuthDto;
import com.lesliefernsby.taskmanager.auth.models.Role;
import com.lesliefernsby.taskmanager.auth.models.User;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MockUserService {

    private final Map<String, User> users;

    public MockUserService() {
        users = new HashMap<>();
        users.put("admin", new User("admin", "password", List.of(Role.ADMIN)));
        users.put("user", new User("user", "password", List.of(Role.USER)));
    }

    private Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }
    public User authenticate(AuthDto authDto) {
        return findByUsername(authDto.getUsername())
                .orElseThrow(() -> new AuthenticationException("User not found"));
    }
}
