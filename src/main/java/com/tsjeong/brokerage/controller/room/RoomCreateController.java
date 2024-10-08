package com.tsjeong.brokerage.controller.room;

import com.tsjeong.brokerage.aop.annotation.JWT;
import com.tsjeong.brokerage.aop.annotation.TokenValidate;
import com.tsjeong.brokerage.aop.annotation.UserIdInject;
import com.tsjeong.brokerage.dto.ResponseDto;
import com.tsjeong.brokerage.dto.room.mapper.RoomMapper;
import com.tsjeong.brokerage.dto.room.request.RoomCreateRequest;
import com.tsjeong.brokerage.dto.room.response.RoomDetailResponse;
import com.tsjeong.brokerage.service.room.command.RoomCreateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "방 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomCreateController {
    private final RoomCreateService roomCreateService;

    @Operation(
            summary = "내방 등록",
            description = "내방 정보를 등록한다. 거래유형은 서로다른 2개 까지만 등록 가능하다."
    )
    @PostMapping
    @TokenValidate
    public ResponseEntity<ResponseDto<RoomDetailResponse>> createRoom(
            @JWT @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @Parameter(hidden = true) @UserIdInject Long actionUserId,
            @RequestBody @Valid RoomCreateRequest requestBody
    ) {

        var room = roomCreateService.createRoom(
                actionUserId,
                requestBody.getRoomTypeId(),
                requestBody.getAddressJibun(),
                requestBody.getAddressRoad(),
                requestBody.getAddressDetail(),
                requestBody.getDescription(),
                requestBody.getTransactions()
        );

        var response = RoomMapper.toRoomDetailResponse(room, actionUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.success(response));
    }
}
