package com.tsjeong.brokerage.dto.room.response;

import com.tsjeong.brokerage.entity.room.Room;
import lombok.Getter;


@Getter
public class RoomDetailResponse extends RoomAbbrResponse {
    private final String description;

    public RoomDetailResponse(Room room) {
        super(room);
        description = room.getDetail() == null ? null : room.getDetail().getDescription();
    }
}
