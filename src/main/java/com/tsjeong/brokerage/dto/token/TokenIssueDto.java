package com.tsjeong.brokerage.dto.token;


import java.time.ZonedDateTime;

public record TokenIssueDto (
    String token,
    ZonedDateTime expiredAt,
    String timeZone
) {}
