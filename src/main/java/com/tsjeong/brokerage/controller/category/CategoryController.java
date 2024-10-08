package com.tsjeong.brokerage.controller.category;

import com.tsjeong.brokerage.aop.annotation.JWT;
import com.tsjeong.brokerage.aop.annotation.TokenValidate;
import com.tsjeong.brokerage.dto.ResponseDto;
import com.tsjeong.brokerage.dto.category.CategoryResponse;
import com.tsjeong.brokerage.service.category.RoomTypeReadService;
import com.tsjeong.brokerage.service.category.TransactionTypeReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "카테고리")
@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final RoomTypeReadService roomTypeReadService;
    private final TransactionTypeReadService transactionTypeReadService;

    @Operation(summary = "방 유형 카테고리 조회")
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


    @Operation(summary = "방 거래 유형 카테고리 조회")
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
