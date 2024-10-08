package com.tsjeong.brokerage.service.room.command;

import com.tsjeong.brokerage.dto.room.request.RoomTransactionUpdateRequest;
import com.tsjeong.brokerage.entity.room.Room;
import com.tsjeong.brokerage.entity.room.RoomTransaction;
import com.tsjeong.brokerage.entity.room.RoomType;
import com.tsjeong.brokerage.exception.ErrorCode;
import com.tsjeong.brokerage.repsoitory.room.RoomRepository;
import com.tsjeong.brokerage.service.category.RoomTypeReadService;
import com.tsjeong.brokerage.service.util.StringValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomUpdateService {

    private final RoomRepository roomRepository;
    private final RoomTypeReadService roomTypeReadService;
    private final RoomTransactionUpdateService roomTransactionUpdateService;


    @Transactional
    public Room roomUpdate(
            long userId,
            long roomId,
            int roomTypeId,
            String addressJibun,
            String addressRoad,
            String addressDetail,
            String description,
            List<RoomTransactionUpdateRequest> transactionsDtos
    ) {

        Room room = roomRepository.findRoomForUpdateUsingId(roomId).orElseThrow(
                () -> ErrorCode.ENTITY_NOT_FOUND.build("'id:%d'인 방을 찾지 못했습니다."));

        RoomType roomType = roomTypeReadService.getRoomTypeById(roomTypeId);

        if (!room.getUser().getId().equals(userId)) {
            throw ErrorCode.USER_NOT_ACCESSIBLE.build("해당 방 정보를 수정할 권한이 없습니다.");
        }

        if (StringValidator.isEmptyOrNull(addressJibun) && StringValidator.isEmptyOrNull(addressRoad)) {
            throw ErrorCode.BAD_CONSTRAINT_BODY_FIELD.build("지번주소 또는 도로명주소 중 하나는 필수입니다.");
        }

        if (transactionsDtos == null || transactionsDtos.isEmpty()) {
            throw ErrorCode.ENTITY_REGISTER_CONFLICT.build("하나의 방은 적어도 1개 이상의 거래 유형을 가져야 합니다.");
        }

        if (transactionsDtos.size() > RoomTransaction.MAX_PER_ROOM) {
            throw ErrorCode.ENTITY_REGISTER_CONFLICT.build("하나의 방은 최대 %d개의 거래 유형만 가질 수 있습니다.".formatted(RoomTransaction.MAX_PER_ROOM));
        }

        if (StringValidator.isEmptyOrNull(description)) {
            room.deleteDetail();
        } else {
            room.updateDetail(description);
        }


        room.update(roomType, addressJibun, addressRoad, addressDetail);
        roomTransactionUpdateService.updateRoomTransactionBy(room, transactionsDtos);
        return roomRepository.save(room);
    }
}
