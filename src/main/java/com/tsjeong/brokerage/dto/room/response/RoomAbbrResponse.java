package com.tsjeong.brokerage.dto.room.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tsjeong.brokerage.entity.room.Room;
import com.tsjeong.brokerage.entity.room.RoomType;
import lombok.Data;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class RoomAbbrResponse {
    private String id;

    private Integer roomTypeId;
    private String roomTypeName;

    private String addressJibun;
    private String addressRoad;
    private String addressDetail;

    @Size(max = 2)
    private List<RoomTransactionResponse> transactions = new ArrayList<>();

    private LocalDate postedAt;

    @JsonIgnore
    public static RoomAbbrResponse of(Room room) {
        RoomAbbrResponse dto = new RoomAbbrResponse();
        dto.setId(Objects.isNull(room.getId()) ? null : room.getId().toString());


        RoomType roomType = room.getRoomType();
        dto.setRoomTypeId(Objects.isNull(roomType) ? null : roomType.getId());
        dto.setRoomTypeName(Objects.isNull(roomType) ? null : roomType.getName());

        dto.setAddressJibun(room.getAddressJibun());
        dto.setAddressRoad(room.getAddressRoad());
        dto.setAddressDetail(room.getAddressDetail());

        dto.setTransactions(
                room.getTransactions().stream()
                .map(RoomTransactionResponse::of)
                .toList()
        );

        dto.setPostedAt(room.getCreatedDateKST());

        return dto;
    }
}
