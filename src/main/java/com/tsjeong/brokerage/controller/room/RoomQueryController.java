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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomQueryController {
    private final RoomQueryDetailService roomQueryDetailService;
    private final RoomQueryPageService roomQueryPageService;

    @GetMapping("/{roomId}")
    @TokenValidate
    public ResponseEntity<ResponseDto<RoomDetailResponse>> getDetailRoom(
            @JWT @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @UserIdInject Long actionUserId,
            @PathVariable Long roomId
    ) {

        var response = roomQueryDetailService.getRoomForDetailQuery(actionUserId, roomId);

        return ResponseEntity.ok(ResponseDto.success(response));
    }


    @GetMapping
    @TokenValidate
    public ResponseEntity<ResponseDto<List<RoomAbbrResponse>>> getRooms(
            @JWT @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @UserIdInject Long actionUserId,
            @RequestParam(required = false, defaultValue = "9223372036854775807") Long lastRoomId,
            @RequestParam @Min(1) @Max(50) int pageSize,
            @RequestParam(required = false) List<Integer> roomTypeIds,
            @RequestParam(required = false) List<Integer> transactionTypeIds,
            @RequestParam(required = false) @Min(0) BigDecimal minRent,
            @RequestParam(required = false) BigDecimal maxRent,
            @RequestParam(required = false) @Min(0) BigDecimal minDeposit,
            @RequestParam(required = false) BigDecimal maxDeposit,
            @RequestParam RoomQueryMode mode
    ) {
        var responses = roomQueryPageService.getRoomsPageBy(
                actionUserId, lastRoomId, pageSize, roomTypeIds, transactionTypeIds, minRent, maxRent, minDeposit, maxDeposit, mode);

        return ResponseEntity.ok(ResponseDto.success(responses));
    }

}
