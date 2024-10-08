package com.tsjeong.brokerage.service.room.command;

import com.tsjeong.brokerage.dto.room.request.RoomTransactionCreateRequest;
import com.tsjeong.brokerage.entity.room.Room;
import com.tsjeong.brokerage.entity.room.RoomDetail;
import com.tsjeong.brokerage.entity.room.RoomType;
import com.tsjeong.brokerage.entity.user.Users;
import com.tsjeong.brokerage.exception.ApplicationException;
import com.tsjeong.brokerage.exception.ErrorCode;
import com.tsjeong.brokerage.repsoitory.room.RoomRepository;
import com.tsjeong.brokerage.service.category.RoomTypeReadService;
import com.tsjeong.brokerage.service.user.UserReadService;
import com.tsjeong.brokerage.service.util.StringValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class RoomCreateService {
    private final UserReadService userReadService;
    private final RoomTypeReadService roomTypeReadService;
    private final RoomTransactionCreateService roomTransactionCreateService;
    private final RoomRepository roomRepository;


    @Transactional
    public Room createRoom(
            long userId,
            int roomTypeId,
            String addressJibun,
            String addressRoad,
            String addressDetail,
            String detail,
            List<RoomTransactionCreateRequest> transactionsDtoList
    ) {

        Users user = userReadService.getUsersById(userId);
        RoomType roomType = roomTypeReadService.getRoomTypeById(roomTypeId);

        if (StringValidator.isEmptyOrNull(addressJibun) && StringValidator.isEmptyOrNull(addressRoad)) {
            throw ErrorCode.BAD_CONSTRAINT_BODY_FIELD.build("지번주소 또는 도로명주소 중 하나는 필수입니다.");
        }

        RoomDetail roomDetail = RoomDetail.builder()
                .description(detail)
                .build();

        Room room = Room.builder()
                .user(user)
                .roomType(roomType)
                .addressJibun(addressJibun)
                .addressRoad(addressRoad)
                .addressDetail(addressDetail)
                .detail(roomDetail)
                .build();

        room = roomRepository.save(room);

        room.getTransactions().addAll(
                roomTransactionCreateService.createRoomTransactionBy(room, transactionsDtoList)
        );

        return room;
    }

}
