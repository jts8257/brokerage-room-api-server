package com.tsjeong.brokerage.controller.room;

import com.tsjeong.brokerage.aop.annotation.JWT;
import com.tsjeong.brokerage.aop.annotation.TokenValidate;
import com.tsjeong.brokerage.aop.annotation.UserIdInject;
import com.tsjeong.brokerage.dto.ResponseDto;
import com.tsjeong.brokerage.service.room.command.RoomDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomDeleteController {
    private final RoomDeleteService roomDeleteService;

    @DeleteMapping("/{roomId}")
    @TokenValidate
    public ResponseEntity<ResponseDto<Boolean>> deleteRoom(
            @JWT @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @UserIdInject Long actionUserId,
            @PathVariable Long roomId
    ) {

        roomDeleteService.deleteRoom(actionUserId, roomId);
        return ResponseEntity.ok(ResponseDto.success(true));
    }
}
