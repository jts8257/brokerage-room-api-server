package com.tsjeong.brokerage.slice.service.token.validate;

import com.tsjeong.brokerage.config.JwtConfig;
import com.tsjeong.brokerage.dto.token.TokenIssueResponse;
import com.tsjeong.brokerage.exception.ApplicationException;
import com.tsjeong.brokerage.service.token.issue.JjwtIssuer;
import com.tsjeong.brokerage.service.token.issue.TokenIssuer;
import com.tsjeong.brokerage.service.token.validate.JjwtValidator;
import com.tsjeong.brokerage.service.token.validate.TokenValidator;
import org.junit.jupiter.api.DisplayName;
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
public class TokenValidatorSliceTest {
    @Autowired
    private TokenIssuer tokenIssuer;

    @Autowired
    private TokenValidator tokenValidator;

    @Test
    @DisplayName("tokenValidator.validate() - 검증 성공")
    void whenValidTokenProvidedReturnValidResult() {
        // Given
        TokenIssueResponse dto = tokenIssuer.issue(Map.of("userId", 1234L));

        // When
        Map<String, Object> payload = tokenValidator.validate(dto.token());

        // Then
        assertEquals(1234L, payload.get("userId"));
    }

    @Test
    @DisplayName("tokenValidator.validate() - 비어있는 jwt 는 예외 발생")
    void whenWrongTokenProvidedThrowException() {
        // Given, When, Then
        assertThrows(ApplicationException.class, () ->  tokenValidator.validate(""));
    }

}
