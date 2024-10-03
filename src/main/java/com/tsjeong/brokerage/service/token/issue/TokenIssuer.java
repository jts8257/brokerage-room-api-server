package com.tsjeong.brokerage.service.token.issue;

import com.tsjeong.brokerage.dto.token.TokenIssueResponse;

import java.util.Map;

public interface TokenIssuer {

    TokenIssueResponse issue(Map<String, Object> payload);
}
