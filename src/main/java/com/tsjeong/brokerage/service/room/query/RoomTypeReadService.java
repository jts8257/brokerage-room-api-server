package com.tsjeong.brokerage.service.room.query;

import com.tsjeong.brokerage.entity.room.RoomType;
import com.tsjeong.brokerage.exception.ErrorCode;
import com.tsjeong.brokerage.repsoitory.room.RoomTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RoomTypeReadService {

    private final RoomTypeRepository roomTypeRepository;

    /**
     *
     * @param roomTypeId 조회하려는 RoomType 의 id
     * @return RoomType
     * @throws com.tsjeong.brokerage.exception.ApplicationException 해당 id 의 roomType 이 존재하지 않을 떄
     * @throws IllegalArgumentException roomTypeId 가 null 일때
     */
    public RoomType getRoomTypeById(Integer roomTypeId) {

        if (Objects.isNull(roomTypeId)) {
            throw new IllegalArgumentException("조회하려는 room type 의 id 가 null 입니다.");
        }

        return roomTypeRepository.findById(roomTypeId).orElseThrow(
                () -> ErrorCode.ENTITY_NOT_FOUND.build("'id:%d'인 방 유형을 찾을 수 없습니다.".formatted(roomTypeId)));
    }
}
