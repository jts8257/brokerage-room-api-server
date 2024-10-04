package com.tsjeong.brokerage.dto.user;

public record UserReadResponse(
        long id,
        String name,
        String email,
        String password
) {}
