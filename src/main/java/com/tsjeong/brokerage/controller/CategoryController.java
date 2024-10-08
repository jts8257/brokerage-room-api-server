package com.tsjeong.brokerage.controller;

import com.tsjeong.brokerage.aop.annotation.JWT;
import com.tsjeong.brokerage.aop.annotation.TokenValidate;
import com.tsjeong.brokerage.dto.ResponseDto;
import com.tsjeong.brokerage.dto.category.CategoryResponse;
import com.tsjeong.brokerage.service.category.RoomTypeReadService;
import com.tsjeong.brokerage.service.category.TransactionTypeReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final RoomTypeReadService roomTypeReadService;
    private final TransactionTypeReadService transactionTypeReadService;

    @TokenValidate(isUserIdInject = false)
    @GetMapping("/categories/room-types")
    public ResponseEntity<ResponseDto<List<CategoryResponse>>> getRoomTypes(
            @JWT @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    ) {

        List<CategoryResponse> categories = roomTypeReadService.getAllRoomTypes().stream()
                .map(entity -> new CategoryResponse(entity.getId(), entity.getName()))
                .toList();

        return ResponseEntity.ok(ResponseDto.success(categories));
    }


    @TokenValidate(isUserIdInject = false)
    @GetMapping("/categories/transaction-types")
    public ResponseEntity<ResponseDto<List<CategoryResponse>>> getTransactionTypes(
            @JWT @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    ) {

        List<CategoryResponse> categories = transactionTypeReadService.getAllTransactionTypes().stream()
                .map(entity -> new CategoryResponse(entity.getId(), entity.getName()))
                .toList();

        return ResponseEntity.ok(ResponseDto.success(categories));
    };

}
