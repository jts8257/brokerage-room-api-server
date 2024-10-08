package com.tsjeong.brokerage.dto.room.mapper;

import com.tsjeong.brokerage.dto.room.response.RoomAbbrResponse;
import com.tsjeong.brokerage.dto.room.response.RoomDetailResponse;
import com.tsjeong.brokerage.entity.room.Room;
import com.tsjeong.brokerage.entity.room.RoomDetail;
import com.tsjeong.brokerage.entity.room.RoomTransaction;
import com.tsjeong.brokerage.entity.room.RoomType;
import com.tsjeong.brokerage.entity.user.Users;
import com.tsjeong.brokerage.exception.ErrorCode;

import java.util.List;
import java.util.Objects;

public class RoomMapper {
    public static RoomAbbrResponse toRoomAbbrResponse(Room room, long userId) {
        RoomType roomType = room.getRoomType();
        Users users = room.getUser();
        List<RoomTransaction> roomTransactions = room.getTransactions();

        validate(roomType,users, roomTransactions);

        return new RoomAbbrResponse(
                Objects.isNull(room.getId()) ? null : room.getId().toString(),
                roomType.getId(),
                roomType.getName(),
                room.getAddressJibun(),
                room.getAddressRoad(),
                room.getAddressDetail(),
                room.getTransactions().stream()
                        .map(RoomTransactionMapper::toResponse)
                        .toList(),
                room.getCreatedAtKST(),
                room.getUpdatedAtKST(),
                users.getNickName(),
                Objects.equals(users.getId(), userId),
                Objects.equals(users.getId(), userId)
        );
    }

    public static RoomDetailResponse toRoomDetailResponse(Room room, long userId) {
        RoomType roomType = room.getRoomType();
        Users users = room.getUser();
        List<RoomTransaction> roomTransactions = room.getTransactions();
        RoomDetail roomDetail = room.getDetail();

        validate(roomType,users, roomTransactions);

        return new RoomDetailResponse(
                Objects.isNull(room.getId()) ? null : room.getId().toString(),
                roomType.getId(),
                roomType.getName(),
                room.getAddressJibun(),
                room.getAddressRoad(),
                room.getAddressDetail(),
                room.getTransactions().stream()
                        .map(RoomTransactionMapper::toResponse)
                        .toList(),
                room.getCreatedAtKST(),
                room.getUpdatedAtKST(),
                users.getNickName(),
                roomDetail != null ? roomDetail.getDescription() : null,
                Objects.equals(users.getId(), userId),
                Objects.equals(users.getId(), userId)
        );
    }

    private static void validate(RoomType roomType, Users users, List<RoomTransaction> roomTransactions) {
        if (Objects.isNull(roomType)) {
            throw ErrorCode.MISSING_REQUIRED_RELATION.build("방의 '방 유형' 정보가 누락되었습니다.");
        }

        if (Objects.isNull(users)) {
            throw ErrorCode.MISSING_REQUIRED_RELATION.build("방의 '등록자' 정보가 누락되었습니다.");
        }

        if (Objects.isNull(roomTransactions) || roomTransactions.isEmpty()) {
            throw ErrorCode.MISSING_REQUIRED_RELATION.build("방의 '거래유형' 정보가 누락되었습니다.");
        }

        if (roomTransactions.size() > RoomTransaction.MAX_PER_ROOM) {
            throw ErrorCode.ENTITY_READ_CONFLICT.build("방의 '거래유형' 은 '%d개' 이하까지만 등록할 수 있지만, '%d개'가 등록되어 있습니다."
                    .formatted(RoomTransaction.MAX_PER_ROOM, roomTransactions.size()));
        }
    }
}
