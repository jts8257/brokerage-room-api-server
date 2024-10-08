package com.tsjeong.brokerage.dto.room.mapper;

import com.tsjeong.brokerage.dto.room.response.RoomAbbrResponse;
import com.tsjeong.brokerage.dto.room.response.RoomDetailResponse;
import com.tsjeong.brokerage.entity.room.Room;
import com.tsjeong.brokerage.entity.room.RoomDetail;
import com.tsjeong.brokerage.entity.room.RoomType;
import com.tsjeong.brokerage.entity.user.Users;

import java.util.Objects;

public class RoomMapper {
    public static RoomAbbrResponse toRoomAbbrResponse(Room room, long userId) {
        RoomType roomType = room.getRoomType();
        Users users = room.getUser();

        return new RoomAbbrResponse(
                Objects.isNull(room.getId()) ? null : room.getId().toString(),
                Objects.isNull(roomType) ? null : roomType.getId(),
                Objects.isNull(roomType) ? null : roomType.getName(),
                room.getAddressJibun(),
                room.getAddressRoad(),
                room.getAddressDetail(),
                room.getTransactions().stream()
                        .map(RoomTransactionMapper::toResponse)
                        .toList(),
                room.getCreatedAtKST(),
                room.getUpdatedAtKST(),
                users != null ? users.getNickName() : null,
                users != null && Objects.equals(users.getId(), userId),
                users != null && Objects.equals(users.getId(), userId)
        );
    }

    public static RoomDetailResponse toRoomDetailResponse(Room room, long userId) {
        RoomType roomType = room.getRoomType();
        Users users = room.getUser();
        RoomDetail roomDetail = room.getDetail();

        return new RoomDetailResponse(
                Objects.isNull(room.getId()) ? null : room.getId().toString(),
                Objects.isNull(roomType) ? null : roomType.getId(),
                Objects.isNull(roomType) ? null : roomType.getName(),
                room.getAddressJibun(),
                room.getAddressRoad(),
                room.getAddressDetail(),
                room.getTransactions().stream()
                        .map(RoomTransactionMapper::toResponse)
                        .toList(),
                room.getCreatedAtKST(),
                room.getUpdatedAtKST(),
                users != null ? users.getNickName() : null,
                roomDetail != null ? roomDetail.getDescription() : null,
                users != null && Objects.equals(users.getId(), userId),
                users != null && Objects.equals(users.getId(), userId)
        );
    }
}
