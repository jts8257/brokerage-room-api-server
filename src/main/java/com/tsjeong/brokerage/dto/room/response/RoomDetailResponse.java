package com.tsjeong.brokerage.dto.room.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tsjeong.brokerage.entity.room.Room;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;


@Data
@EqualsAndHashCode(callSuper = true)
public class RoomDetailResponse extends RoomAbbrResponse {
    private String description;

    @JsonIgnore
    public static RoomDetailResponse of(Room room) {
        RoomDetailResponse dto = new RoomDetailResponse();

        BeanUtils.copyProperties(RoomAbbrResponse.of(room), dto);

        dto.setDescription(room.getDetail().getDescription());

        return dto;
    }
}
