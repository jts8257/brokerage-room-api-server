package com.tsjeong.brokerage.service.token;

import com.tsjeong.brokerage.dto.token.TokenIssueDto;
import com.tsjeong.brokerage.repsoitory.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class TokenIssueService {

    private final UsersRepository usersRepository;
    private final TokenIssuer tokenIssuer;

    public TokenIssueDto
}
