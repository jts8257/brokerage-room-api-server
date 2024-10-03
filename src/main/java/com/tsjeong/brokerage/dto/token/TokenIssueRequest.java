package com.tsjeong.brokerage.dto.token;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TokenIssueRequest {
    @NotNull @NotBlank
    private String email;
    @NotNull @NotBlank
    private String password;
}
