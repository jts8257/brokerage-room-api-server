package com.tsjeong.brokerage.slice.service.token.issue;


import com.tsjeong.brokerage.config.JwtConfig;
import com.tsjeong.brokerage.dto.token.TokenIssueResponse;
import com.tsjeong.brokerage.exception.ApplicationException;
import com.tsjeong.brokerage.service.token.issue.JjwtIssuer;
import com.tsjeong.brokerage.service.token.issue.TokenIssuer;
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
public class TokenIssuerTest {

    @Autowired
    private TokenIssuer tokenIssuer;

    @Test
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

        assertEquals("UTC", tokenIssueResponse.timeZone());
    }

    @Test
    void shouldThrowErrorWhenNullClaimProvided() {
        // Given, When, Then
        assertThrows(ApplicationException.class, () ->  tokenIssuer.issue(null));
    }

    @Test
    void shouldThrowErrorWhenEmptyClaimProvided() {
        // Given, When, Then
        assertThrows(ApplicationException.class, () ->  tokenIssuer.issue(Map.of()));
    }
}
