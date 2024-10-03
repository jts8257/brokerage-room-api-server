package com.tsjeong.brokerage.service.token.issue;

import com.tsjeong.brokerage.dto.token.TokenIssueDto;
import com.tsjeong.brokerage.exception.ErrorCode;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Component
public class JjwtIssuer implements TokenIssuer {
    private final SecretKey jwsSignKey;
    private final Long jwtLifeTime;

    public JjwtIssuer(
            @Qualifier("jwsSignKey") SecretKey jwsSignKey,
            @Qualifier("jwtLifeTime") Long jwtLifeTime
    ) {
        this.jwsSignKey = jwsSignKey;
        this.jwtLifeTime = jwtLifeTime;
    }

    @Override
    public TokenIssueDto issue(Map<String, Object> claims) {
        if (Objects.isNull(claims) || claims.isEmpty()) {
            throw ErrorCode.INVALID_TOKEN_PROVIDE_PARAM.build("인증 수단은 적어도 하나의 payload 를 포함하고 있어야 합니다.");
        }

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime expiration = now.plusSeconds(jwtLifeTime);
        String token = Jwts.builder()
                .claims(claims)
                .issuer("API-Server")
                .issuedAt(Date.from(now.toInstant()))
                .expiration(Date.from(expiration.toInstant()))
                .signWith(jwsSignKey)
                .compact();

        return new TokenIssueDto(
                token,
                expiration,
                expiration.getZone().getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
        );
    }
}
