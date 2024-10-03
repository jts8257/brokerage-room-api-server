package com.tsjeong.brokerage.controller;

import com.tsjeong.brokerage.aop.annotation.TokenValidate;
import com.tsjeong.brokerage.aop.annotation.UserIdInject;
import com.tsjeong.brokerage.dto.ResponseDto;
import com.tsjeong.brokerage.dto.room.request.RoomCreateRequest;
import com.tsjeong.brokerage.dto.room.request.RoomUpdateRequest;
import com.tsjeong.brokerage.dto.room.response.RoomAbbrResponse;
import com.tsjeong.brokerage.dto.room.response.RoomDetailResponse;
import com.tsjeong.brokerage.service.room.query.enums.RoomQueryMode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    @PostMapping
    @TokenValidate
    public ResponseEntity<ResponseDto<RoomDetailResponse>> createRoom(
            @UserIdInject Long actionUserId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @RequestBody @Valid RoomCreateRequest requestBody
    ) {
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{roomId}")
    @TokenValidate
    public ResponseEntity<ResponseDto<RoomDetailResponse>> getDetailRoom(
            @UserIdInject Long actionUserId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @PathVariable Long roomId
    ) {
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    @TokenValidate
    public ResponseEntity<ResponseDto<List<RoomAbbrResponse>>> getRooms(
            @UserIdInject Long actionUserId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @RequestParam(required = true, defaultValue = "ALL") RoomQueryMode mode
    ) {
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{roomId}")
    @TokenValidate
    public ResponseEntity<ResponseDto<RoomDetailResponse>> updateRoom(
            @UserIdInject Long actionUserId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @PathVariable Long roomId,
            @RequestBody RoomUpdateRequest requestBody
    ) {
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{roomId}")
    @TokenValidate
    public ResponseEntity<ResponseDto<RoomDetailResponse>> deleteRoom(
            @UserIdInject Long actionUserId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @PathVariable Long roomId
    ) {
        return ResponseEntity.notFound().build();
    }
}
