package com.tsjeong.brokerage.controller;

import com.tsjeong.brokerage.dto.ResponseDto;
import com.tsjeong.brokerage.dto.token.TokenIssueResponse;
import com.tsjeong.brokerage.dto.token.TokenIssueRequest;
import com.tsjeong.brokerage.service.token.issue.TokenIssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tokens")
public class TokenController {

    private final TokenIssueService tokenIssueService;

    @PostMapping("/issue")
    public ResponseEntity<ResponseDto<TokenIssueResponse>> issueToken(
            @Valid @RequestBody TokenIssueRequest requestBody
    ) {
        return ResponseEntity.ok(
                ResponseDto.success(
                        tokenIssueService.issueTokenBy(requestBody.getEmail(), requestBody.getPassword())
                )
        );

    }
}
