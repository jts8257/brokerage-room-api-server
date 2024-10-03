package com.tsjeong.brokerage.dto.user;

public record UserReadDto (
        long id,
        String email,
        String password
) {}
