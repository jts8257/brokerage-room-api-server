package com.tsjeong.brokerage.controller;

import com.tsjeong.brokerage.aop.annotation.TokenValidate;
import com.tsjeong.brokerage.aop.annotation.UserIdInject;
import com.tsjeong.brokerage.dto.ResponseDto;
import com.tsjeong.brokerage.dto.room.request.RoomCreateRequest;
import com.tsjeong.brokerage.dto.room.request.RoomUpdateRequest;
import com.tsjeong.brokerage.dto.room.response.RoomAbbrResponse;
import com.tsjeong.brokerage.dto.room.response.RoomDetailResponse;
import com.tsjeong.brokerage.entity.room.Room;
import com.tsjeong.brokerage.service.room.command.RoomCreateService;
import com.tsjeong.brokerage.service.room.query.RoomQueryDetailService;
import com.tsjeong.brokerage.service.room.query.RoomQueryPageService;
import com.tsjeong.brokerage.service.room.query.enums.RoomQueryMode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {
    private final RoomCreateService roomCreateService;
    private final RoomQueryDetailService roomQueryDetailService;
    private final RoomQueryPageService roomQueryPageService;

    @PostMapping
    @TokenValidate
    public ResponseEntity<ResponseDto<RoomDetailResponse>> createRoom(
            @UserIdInject Long actionUserId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @RequestBody @Valid RoomCreateRequest requestBody
    ) {
        Room room = roomCreateService.createRoom(
                actionUserId,
                requestBody.getRoomTypeId(),
                requestBody.getAddressJibun(),
                requestBody.getAddressRoad(),
                requestBody.getAddressDetail(),
                requestBody.getDescription(),
                requestBody.getTransactions()
        );

        RoomDetailResponse response = new RoomDetailResponse(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.success(response));
    }

    @GetMapping("/{roomId}")
    @TokenValidate
    public ResponseEntity<ResponseDto<RoomDetailResponse>> getDetailRoom(
            @UserIdInject Long actionUserId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @PathVariable Long roomId
    ) {
        RoomDetailResponse response = roomQueryDetailService.getRoomForDetailQuery(roomId);

        return ResponseEntity.ok(ResponseDto.success(response));
    }


    @GetMapping
    @TokenValidate
    public ResponseEntity<ResponseDto<List<RoomAbbrResponse>>> getRooms(
            @UserIdInject Long actionUserId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
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
        List<RoomAbbrResponse> responses = roomQueryPageService.getRoomsPageBy(
                actionUserId, lastRoomId, pageSize, roomTypeIds, transactionTypeIds, minRent, maxRent, minDeposit, maxDeposit, mode);

        return ResponseEntity.ok(ResponseDto.success(responses));
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
