package com.tsjeong.brokerage.controller;

import com.tsjeong.brokerage.dto.ResponseDto;
import com.tsjeong.brokerage.dto.user.UserReadResponse;
import com.tsjeong.brokerage.entity.user.Users;
import com.tsjeong.brokerage.service.user.UserReadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserReadService userReadService;


    @GetMapping("/users")
    public ResponseEntity<ResponseDto<List<UserReadResponse>>> getAllUsers() {
        List<UserReadResponse> usersList = userReadService.getAllUsers();
        log.info(usersList.toString());
        return ResponseEntity.ok(
                ResponseDto.success(
                        usersList
                ));
    }
}
