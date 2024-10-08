package com.tsjeong.brokerage.slice.service.token.validate;

import com.tsjeong.brokerage.aop.annotation.JWT;
import com.tsjeong.brokerage.aop.annotation.TokenValidate;
import com.tsjeong.brokerage.aop.annotation.UserIdInject;
import com.tsjeong.brokerage.aop.aspect.TokenValidateAspect;
import com.tsjeong.brokerage.config.JwtConfig;
import com.tsjeong.brokerage.exception.ApplicationAdvice;
import com.tsjeong.brokerage.exception.ApplicationException;
import com.tsjeong.brokerage.exception.ErrorCode;
import com.tsjeong.brokerage.service.token.issue.JjwtIssuer;
import com.tsjeong.brokerage.service.token.validate.JjwtValidator;
import com.tsjeong.brokerage.service.token.validate.TokenValidateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(
        classes = {
                JwtConfig.class,
                JjwtIssuer.class,
                JjwtValidator.class,
                TokenValidateAspect.class,
                TokenValidateService.class,
                ApplicationAdvice.class,
                AOPValidationSliceTest.TokenAOPService.class
        }
)
@EnableAspectJAutoProxy
@ActiveProfiles("test")
@EnableConfigurationProperties(JwtConfig.class)
class AOPValidationSliceTest {


    @Autowired
    private JjwtIssuer tokenIssue;

    @Autowired
    private TokenAOPService tokenAOPService;

    private String token;
    @BeforeEach
    void setUp() {
        var tokenResponse = tokenIssue.issue(Map.of("userId", 1L));
        token = "Bearer " + tokenResponse.token();
    }

    @Test
    void tokenValidAOPWorks() {
        assertEquals(1L, tokenAOPService.inject(token, 0L));
    }

    @Test
    void shouldThrowExceptionWhenAllAnnotationMissing() {
        ApplicationException ape = assertThrows(ApplicationException.class, () ->tokenAOPService.bothMissing(token, 0L));
        assertEquals(ErrorCode.TOKEN_NOT_EXISTS.getCode(), ape.getCode());

    }

    @Test
    void shouldThrowExceptionWhenOneAnnotationMissing() {
        assertThrows(
                IllegalArgumentException.class, ()->
                tokenAOPService.notUserInject(token, 0L)
        );
    }

    @Service
    public static class TokenAOPService {

        @TokenValidate
        public Long bothMissing(
                String token,
                Long userId
        ) {
            return userId;
        }

        @TokenValidate
        public Long notUserInject(
                @JWT String token,
                Long userId
        ) {
            return userId;
        }

        @TokenValidate
        public Long inject(
                @JWT String token,
                @UserIdInject Long userId
        ) {
            return userId;
        }
    }
}
