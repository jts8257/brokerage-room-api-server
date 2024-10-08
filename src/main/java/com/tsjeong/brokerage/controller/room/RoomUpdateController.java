package com.tsjeong.brokerage.controller.room;

import com.tsjeong.brokerage.aop.annotation.JWT;
import com.tsjeong.brokerage.aop.annotation.TokenValidate;
import com.tsjeong.brokerage.aop.annotation.UserIdInject;
import com.tsjeong.brokerage.dto.ResponseDto;
import com.tsjeong.brokerage.dto.room.mapper.RoomMapper;
import com.tsjeong.brokerage.dto.room.request.RoomUpdateRequest;
import com.tsjeong.brokerage.dto.room.response.RoomDetailResponse;
import com.tsjeong.brokerage.service.room.command.RoomUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "방 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomUpdateController {
    private final RoomUpdateService roomUpdateService;

    @Operation(
            summary = "내방 수정",
            description = "내가 등록한 방의 정보를 수정한다. 거래유형은 서로다른 2개 까지만 등록 가능하다."
    )
    @PutMapping("/{roomId}")
    @TokenValidate
    public ResponseEntity<ResponseDto<RoomDetailResponse>> updateRoom(
            @JWT @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @Parameter(hidden = true) @UserIdInject Long actionUserId,
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
