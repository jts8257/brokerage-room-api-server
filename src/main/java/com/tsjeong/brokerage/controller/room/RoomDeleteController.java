package com.tsjeong.brokerage.controller.room;

import com.tsjeong.brokerage.aop.annotation.JWT;
import com.tsjeong.brokerage.aop.annotation.TokenValidate;
import com.tsjeong.brokerage.aop.annotation.UserIdInject;
import com.tsjeong.brokerage.dto.ResponseDto;
import com.tsjeong.brokerage.service.room.command.RoomDeleteService;
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
public class RoomDeleteController {
    private final RoomDeleteService roomDeleteService;

    @Operation(
            summary = "내방 삭제",
            description = "내방을 삭제한다"
    )
    @DeleteMapping("/{roomId}")
    @TokenValidate
    public ResponseEntity<ResponseDto<Boolean>> deleteRoom(
            @JWT @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @Parameter(hidden = true) @UserIdInject Long actionUserId,
            @PathVariable Long roomId
    ) {

        roomDeleteService.deleteRoom(actionUserId, roomId);
        return ResponseEntity.ok(ResponseDto.success(true));
    }
}
