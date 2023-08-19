package com.lesliefernsby.taskmanager.auth.controller;

import com.lesliefernsby.taskmanager.auth.exceptions.AuthenticationException;
import com.lesliefernsby.taskmanager.auth.model.AuthDto;
import com.lesliefernsby.taskmanager.auth.model.AuthResponseDto;
import com.lesliefernsby.taskmanager.auth.models.User;
import com.lesliefernsby.taskmanager.auth.services.MockUserService;
import com.lesliefernsby.taskmanager.auth.utils.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private MockUserService userService;

    private JwtTokenProvider tokenProvider;

    public AuthController(MockUserService userService, JwtTokenProvider tokenProvider) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthDto authDto) {
        try {
            // Authenticate the user using your mock service
            User user = userService.authenticate(authDto);

            // Generate tokens using your token provider
            String accessToken = tokenProvider.createAccessToken(user);
            String refreshToken = tokenProvider.createRefreshToken(user);

            // Create the response DTO
            AuthResponseDto response = new AuthResponseDto();
            response.setAuthToken(accessToken);
            response.setRefreshToken(refreshToken);

            // Return the response
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            // Handle authentication failure
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponseDto(null, null, e.getMessage()));
        }
    }
}
