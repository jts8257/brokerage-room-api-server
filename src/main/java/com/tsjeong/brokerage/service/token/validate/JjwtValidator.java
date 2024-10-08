package com.tsjeong.brokerage.service.token.validate;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Map;

import static com.tsjeong.brokerage.exception.ErrorCode.ACCESS_TOKEN_EXPIRED;
import static com.tsjeong.brokerage.exception.ErrorCode.INVALID_TOKEN;
import static com.tsjeong.brokerage.service.token.issue.TokenIssueService.PAYLOAD_USER_ID_KEY;

@Component
public class JjwtValidator implements TokenValidator {

    private final SecretKey jwsSignKey;

    public JjwtValidator(
            @Qualifier("jwsSignKey") SecretKey jwsSignKey
    ) {
        this.jwsSignKey = jwsSignKey;
    }

    @Override
    public Map<String, Object> validate(String token) {

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(jwsSignKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return Map.of(
                    PAYLOAD_USER_ID_KEY, ((Number) claims.get(PAYLOAD_USER_ID_KEY)).longValue()
            );

        } catch (ExpiredJwtException expired) {
            throw ACCESS_TOKEN_EXPIRED.build();

        } catch (Exception ex) {
            throw INVALID_TOKEN.build(ex.getMessage());
        }
    }
}
