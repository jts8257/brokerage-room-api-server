package com.tsjeong.brokerage.service.room.query;

import com.tsjeong.brokerage.dto.room.response.RoomDetailResponse;
import com.tsjeong.brokerage.entity.room.Room;
import com.tsjeong.brokerage.exception.ErrorCode;
import com.tsjeong.brokerage.repsoitory.room.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomQueryDetailService {

    private final RoomRepository roomRepository;


    // Todo 트랜잭션이 필요한가?

    @Transactional(readOnly = true)
    public RoomDetailResponse getRoomForDetailQuery(
            Long roomId
    ) {

        Room room = roomRepository.findRoomByIdFetchDetail(roomId)
                .orElseThrow(() -> ErrorCode.ENTITY_NOT_FOUND.build("'id:%d'에 해당하는 방을 찾을 수 없습니다.".formatted(roomId)));

        return new RoomDetailResponse(room);
    }
}
