package com.tsjeong.brokerage.dto.token;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TokenIssueRequest {

    @Schema(description = "유저 이메일")
    @NotNull @NotBlank
    private String email;

    @Schema(description = "유저 비밀번호")
    @NotNull @NotBlank
    private String password;
}
