package com.tsjeong.brokerage.controller.token;

import com.tsjeong.brokerage.dto.ResponseDto;
import com.tsjeong.brokerage.dto.token.TokenIssueResponse;
import com.tsjeong.brokerage.dto.token.TokenIssueRequest;
import com.tsjeong.brokerage.dto.user.UserReadResponse;
import com.tsjeong.brokerage.service.token.issue.TokenIssueService;
import com.tsjeong.brokerage.service.user.UserReadService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "토큰 발급 및 유저 리스트 조회")
@RestController
@RequiredArgsConstructor
public class TokenController {

    private final TokenIssueService tokenIssueService;
    private final UserReadService userReadService;


    @GetMapping("/users")
    public ResponseEntity<ResponseDto<List<UserReadResponse>>> getAllUsers() {

        List<UserReadResponse> usersList = userReadService.getAllUsers();

        return ResponseEntity.ok(ResponseDto.success(usersList));
    }

    @PostMapping("/tokens/issue")
    public ResponseEntity<ResponseDto<TokenIssueResponse>> issueToken(
            @Valid @RequestBody TokenIssueRequest requestBody
    ) {

        TokenIssueResponse tokenIssueResponse = tokenIssueService.issueTokenBy(
                requestBody.getEmail(), requestBody.getPassword());

        return ResponseEntity.ok(ResponseDto.success(tokenIssueResponse));

    }
}
