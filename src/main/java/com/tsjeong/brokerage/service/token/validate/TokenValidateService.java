package com.tsjeong.brokerage.service.token.validate;

import com.tsjeong.brokerage.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TokenValidateService {
    public final static String TOKEN_PREFIX = "Bearer ";

    private final TokenValidator tokenValidator;

    public Map<String, Object> validateToken(String token) {
        if (Objects.isNull(token)) {
            throw ErrorCode.TOKEN_NOT_EXISTS.build("헤더 %s 의 값이 누락되었습니다.".formatted(HttpHeaders.AUTHORIZATION));
        }

        if (token.startsWith(TOKEN_PREFIX)) {
            token = token.substring(TOKEN_PREFIX.length());
        }

        return tokenValidator.validate(token);
    }
}
