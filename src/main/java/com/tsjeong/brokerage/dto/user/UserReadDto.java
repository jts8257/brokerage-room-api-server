package com.tsjeong.brokerage.dto.user;

public record UserReadDto (
        int id,
        String email,
        String password
) { }
