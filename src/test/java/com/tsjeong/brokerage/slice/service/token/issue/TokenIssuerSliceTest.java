package com.tsjeong.brokerage.slice.service.token.issue;


import com.tsjeong.brokerage.config.JwtConfig;
import com.tsjeong.brokerage.dto.token.TokenIssueResponse;
import com.tsjeong.brokerage.exception.ApplicationException;
import com.tsjeong.brokerage.service.token.issue.JjwtIssuer;
import com.tsjeong.brokerage.service.token.issue.TokenIssuer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        classes = {JwtConfig.class, JjwtIssuer.class}
)
@EnableConfigurationProperties(JwtConfig.class)
@ActiveProfiles("test")
class TokenIssuerSliceTest {

    @Autowired
    private TokenIssuer tokenIssuer;

    @Test
    @DisplayName("TokenIssuer.issue - jwt 발행 성공")
    void shouldReturnValidResultWhenValidClaimProvided() {
        // Given
        Map<String, Object> claims = Map.of("userId", "12345");

        // When
        TokenIssueResponse tokenIssueResponse = tokenIssuer.issue(claims);

        // Then
        assertNotNull(tokenIssueResponse);
        assertNotNull(tokenIssueResponse.token());
        assertNotNull(tokenIssueResponse.expiredAt());
        assertNotNull(tokenIssueResponse.timeZone());
    }

    @Test
    @DisplayName("TokenIssuer.issue - 비어있는 claim 은 jwt 발행시 예외 발생")
    void shouldThrowErrorWhenNullClaimProvided() {
        // Given, When, Then
        assertThrows(ApplicationException.class, () ->  tokenIssuer.issue(null));
    }

    @Test
    @DisplayName("TokenIssuer.issue - 비어있는 claim 은 jwt 발행시 예외 발생")
    void shouldThrowErrorWhenEmptyClaimProvided() {
        // Given, When, Then
        assertThrows(ApplicationException.class, () ->  tokenIssuer.issue(Map.of()));
    }
}
