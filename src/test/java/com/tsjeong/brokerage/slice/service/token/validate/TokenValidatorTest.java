package com.tsjeong.brokerage.slice.service.token.validate;

import com.tsjeong.brokerage.config.JwtConfig;
import com.tsjeong.brokerage.dto.token.TokenIssueResponse;
import com.tsjeong.brokerage.exception.ApplicationException;
import com.tsjeong.brokerage.service.token.issue.JjwtIssuer;
import com.tsjeong.brokerage.service.token.issue.TokenIssuer;
import com.tsjeong.brokerage.service.token.validate.JjwtValidator;
import com.tsjeong.brokerage.service.token.validate.TokenValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(
        classes = {JwtConfig.class, JjwtIssuer.class, JjwtValidator.class}
)
@EnableConfigurationProperties(JwtConfig.class)
@ActiveProfiles("test")
public class TokenValidatorTest {
    @Autowired
    private TokenIssuer tokenIssuer;

    @Autowired
    private TokenValidator tokenValidator;


    @Test
    void whenWrongTokenProvidedThrowException() {
        // When, Given, Then
        assertThrows(ApplicationException.class, () ->  tokenValidator.validate(""));
    }

    @Test
    void whenValidTokenProvidedReturnValidResult() {
        // When
        TokenIssueResponse dto = tokenIssuer.issue(Map.of("userId", 1234L));

        // Given
        Map<String, Object> payload = tokenValidator.validate(dto.token());

        // Then
        assertEquals(1234L, payload.get("userId"));
    }
}
