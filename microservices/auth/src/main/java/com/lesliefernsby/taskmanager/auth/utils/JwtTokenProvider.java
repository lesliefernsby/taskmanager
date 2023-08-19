package com.lesliefernsby.taskmanager.auth.utils;
import com.lesliefernsby.taskmanager.auth.exceptions.AuthenticationException;
import com.lesliefernsby.taskmanager.auth.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.security.Key;

import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenProvider {

    private long accessTokenValidity;
    private long refreshTokenValidity;
    private Key key;

    public JwtTokenProvider(ConfigLoader configLoader) {
        this.key = Keys.hmacShaKeyFor(configLoader.getAuthConfig().getSecretKey().getBytes());
        this.accessTokenValidity = configLoader.getAuthConfig().getAccessTokenValidity();
        this.refreshTokenValidity = configLoader.getAuthConfig().getRefreshTokenValidity();
    }
    public String createAccessToken(User user) {
        return createToken(user, accessTokenValidity);
    }

    public String createRefreshToken(User user) {
        return createToken(user, refreshTokenValidity);
    }

    private String createToken(User user, long validity) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("id", user.getId());
        claims.put("roles", user.getRoles());


        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(Instant.ofEpochSecond(validity)))
                .signWith(key)
                .compact();
    }

    // Methods to validate and extract information from the token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token, User user) {
        final String username = getUsernameFromToken(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getClaimFromToken(token, Claims::getExpiration);
        return expiration.before(new Date());
    }
}
