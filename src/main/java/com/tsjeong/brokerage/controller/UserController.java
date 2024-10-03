package com.tsjeong.brokerage.controller;

import com.tsjeong.brokerage.dto.ResponseDto;
import com.tsjeong.brokerage.dto.user.UserReadDto;
import com.tsjeong.brokerage.service.user.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserQueryService userQueryService;


    @GetMapping("/users")
    public ResponseEntity<ResponseDto<List<UserReadDto>>> getAllUsers() {

        return ResponseEntity.ok(
                ResponseDto.success(
                        userQueryService.getAllUsers()
                ));
    }
}
