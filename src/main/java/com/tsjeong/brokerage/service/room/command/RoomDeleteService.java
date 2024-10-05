package com.tsjeong.brokerage.service.room.command;


import com.tsjeong.brokerage.entity.room.Room;
import com.tsjeong.brokerage.exception.ErrorCode;
import com.tsjeong.brokerage.repsoitory.room.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomDeleteService {

    private final RoomRepository roomRepository;

    public void deleteRoom(
            long userId,
            long roomId
    ) {

        Room room = roomRepository.findRoomByIdFetchUser(roomId).orElseThrow(
                () -> ErrorCode.ENTITY_NOT_FOUND.build("'id:%d'인 방을 찾지 못했습니다."));

        if (!room.getUser().getId().equals(userId)) {
            throw ErrorCode.USER_NOT_ACCESSIBLE.build("해당 방 정보를 삭제할 권한이 없습니다.");
        }

        roomRepository.delete(room);
    }
}
