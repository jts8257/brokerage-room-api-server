package com.tsjeong.brokerage.service.category;

import com.tsjeong.brokerage.entity.room.RoomType;
import com.tsjeong.brokerage.exception.ErrorCode;
import com.tsjeong.brokerage.repsoitory.room.RoomTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RoomTypeReadServiceImpl implements RoomTypeReadService {

    private final RoomTypeRepository roomTypeRepository;

    public RoomType getRoomTypeById(Integer roomTypeId) {

        if (Objects.isNull(roomTypeId)) {
            throw new IllegalArgumentException("조회하려는 room type 의 id 가 null 입니다.");
        }

        return roomTypeRepository.findById(roomTypeId).orElseThrow(
                () -> ErrorCode.ENTITY_NOT_FOUND.build("'id:%d'인 방 유형을 찾을 수 없습니다.".formatted(roomTypeId)));
    }

    public List<RoomType> getAllRoomTypes() {
        return roomTypeRepository.findAll();
    }
}
