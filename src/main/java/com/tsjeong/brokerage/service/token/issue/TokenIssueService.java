package com.tsjeong.brokerage.service.token.issue;

import com.tsjeong.brokerage.dto.token.TokenIssueResponse;
import com.tsjeong.brokerage.entity.user.Users;
import com.tsjeong.brokerage.exception.ErrorCode;
import com.tsjeong.brokerage.repsoitory.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TokenIssueService {

    private final UsersRepository usersRepository;
    private final TokenIssuer tokenIssuer;
    public static String PAYLOAD_USER_ID_KEY = "userId";

    public TokenIssueResponse issueTokenBy(String email, String password) {
        Users users = usersRepository.findByEmail(email)
                .orElseThrow(() -> ErrorCode.USER_NOT_EXISTS.build("email:%s 인 유저는 없습니다.".formatted(email)));

        if (!Objects.equals(password, users.getPassword())) {
            throw ErrorCode.INVALID_PASSWORD.build("비밀번호가 일치하지 않습니다.");
        }

        return tokenIssuer.issue(Map.of(PAYLOAD_USER_ID_KEY, users.getId()));
    }
}
