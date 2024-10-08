package com.tsjeong.brokerage.controller.room;

import com.tsjeong.brokerage.aop.annotation.JWT;
import com.tsjeong.brokerage.aop.annotation.TokenValidate;
import com.tsjeong.brokerage.aop.annotation.UserIdInject;
import com.tsjeong.brokerage.dto.ResponseDto;
import com.tsjeong.brokerage.dto.room.response.RoomAbbrResponse;
import com.tsjeong.brokerage.dto.room.response.RoomDetailResponse;
import com.tsjeong.brokerage.service.room.query.RoomQueryDetailService;
import com.tsjeong.brokerage.service.room.query.RoomQueryPageService;
import com.tsjeong.brokerage.service.room.query.enums.RoomQueryMode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;

@Tag(name = "방 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomQueryController {
    private final RoomQueryDetailService roomQueryDetailService;
    private final RoomQueryPageService roomQueryPageService;

    @Operation(
            summary = "방 세부 조회",
            description = "방 하나의 세부 정보를 조회한다."
    )
    @GetMapping("/{roomId}")
    @TokenValidate
    public ResponseEntity<ResponseDto<RoomDetailResponse>> getDetailRoom(
            @JWT @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @Parameter(hidden = true) @UserIdInject Long actionUserId,
            @PathVariable Long roomId
    ) {

        var response = roomQueryDetailService.getRoomForDetailQuery(actionUserId, roomId);

        return ResponseEntity.ok(ResponseDto.success(response));
    }

    @Operation(
            summary = "내방 혹은 전체 방 조회",
            description = "내가 등록한 방 혹은 다른 사람이 등록한 방을 포함한 전체 방을 조회한다."
    )
    @GetMapping
    @TokenValidate
    public ResponseEntity<ResponseDto<List<RoomAbbrResponse>>> getRooms(
            @JWT @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @Parameter(hidden = true) @UserIdInject Long actionUserId,

            @Parameter(required = true, description = "페이지 마지막에 보인 방의 id")
            @RequestParam(required = false, defaultValue = "9223372036854775807") Long lastRoomId,

            @Parameter(required = true, description = "한번에 조회할 방의 개수(1~50)")
            @RequestParam @Min(1) @Max(50) int pageSize,

            @Parameter(description = "조회 대상 방유형 id, 비어있거나 null 이면 전체 조회")
            @RequestParam(required = false) List<Integer> roomTypeIds,

            @Parameter(description = "조회 대상 거래 유형 id, 비어있거나 null 이면 전체 조회")
            @RequestParam(required = false) List<Integer> transactionTypeIds,

            @Parameter(description = "월세 조회 최소값") @RequestParam(required = false) @Min(0) BigDecimal minRent,
            @Parameter(description = "월세 조회 최대값") @RequestParam(required = false) @Min(1) BigDecimal maxRent,
            @Parameter(description = "보증금 조회 최소값") @RequestParam(required = false) @Min(0) BigDecimal minDeposit,
            @Parameter(description = "보증금 조회 최대값") @RequestParam(required = false) @Min(1) BigDecimal maxDeposit,
            @Parameter(description = "조회 모드 {MY: 내방, ALL: 모든방}") @RequestParam RoomQueryMode mode
    ) {
        var responses = roomQueryPageService.getRoomsPageBy(
                actionUserId, lastRoomId, pageSize, roomTypeIds, transactionTypeIds, minRent, maxRent, minDeposit, maxDeposit, mode);

        return ResponseEntity.ok(ResponseDto.success(responses));
    }

}
