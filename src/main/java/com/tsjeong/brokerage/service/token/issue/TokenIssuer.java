package com.tsjeong.brokerage.service.token;

import java.util.Map;

interface TokenIssuer {

    String issue(Map<String, Object> payload, long lifetime);

    long lifeTimeInSeconds = 60 * 60 * 24 * 7; // 7days access token lifetime
}
