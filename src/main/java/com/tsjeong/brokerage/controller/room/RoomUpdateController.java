package com.tsjeong.brokerage.controller.room;

import com.tsjeong.brokerage.aop.annotation.JWT;
import com.tsjeong.brokerage.aop.annotation.TokenValidate;
import com.tsjeong.brokerage.aop.annotation.UserIdInject;
import com.tsjeong.brokerage.dto.ResponseDto;
import com.tsjeong.brokerage.dto.room.mapper.RoomMapper;
import com.tsjeong.brokerage.dto.room.request.RoomUpdateRequest;
import com.tsjeong.brokerage.dto.room.response.RoomDetailResponse;
import com.tsjeong.brokerage.service.room.command.RoomUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomUpdateController {
    private final RoomUpdateService roomUpdateService;

    @PutMapping("/{roomId}")
    @TokenValidate
    public ResponseEntity<ResponseDto<RoomDetailResponse>> updateRoom(
            @JWT @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @UserIdInject Long actionUserId,
            @PathVariable Long roomId,
            @RequestBody RoomUpdateRequest requestBody
    ) {

        var room = roomUpdateService.roomUpdate(
                actionUserId,
                roomId,
                requestBody.getRoomTypeId(),
                requestBody.getAddressJibun(),
                requestBody.getAddressRoad(),
                requestBody.getAddressDetail(),
                requestBody.getDescription(),
                requestBody.getTransactions());

        var response = RoomMapper.toRoomDetailResponse(room, actionUserId);

        return ResponseEntity.ok(ResponseDto.success(response));
    }
}
