package com.tsjeong.brokerage.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
@ConfigurationProperties(prefix = "spring.encryption.jwt")
public class JwtConfig {
    private String secretKey;
    private long lifetime;
    void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
    void setLifetime(long lifetime) {
        this.lifetime = lifetime;
    }
    @Bean(name = "jwsSignKey")
    public SecretKey jwsSignKey() {
        byte[] keyBytes = Base64.getEncoder().encodeToString(secretKey.getBytes()).getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);  // HS256에 맞는 SecretKey 생성
    }

    @Bean(name = "jwtLifeTime")
    public Long jwtLifeTime () {
        return lifetime;
    }
}
