package com.tsjeong.brokerage.dto.token;


import java.time.ZonedDateTime;

public record TokenIssueResponse(
    String token,
    ZonedDateTime expiredAt,
    String timeZone
) {}
