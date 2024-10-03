package com.tsjeong.brokerage.service.token.issue;

import com.tsjeong.brokerage.dto.token.TokenIssueDto;

import java.util.Map;

public interface TokenIssuer {

    TokenIssueDto issue(Map<String, Object> payload);
}
