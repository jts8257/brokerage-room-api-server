package com.tsjeong.brokerage.dto.room.response;

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

    public RoomAbbrResponse (Room room) {

        id = (Objects.isNull(room.getId()) ? null : room.getId().toString());

        RoomType roomType = room.getRoomType();
        roomTypeId = (Objects.isNull(roomType) ? null : roomType.getId());
        roomTypeName = (Objects.isNull(roomType) ? null : roomType.getName());

        addressJibun = room.getAddressJibun();
        addressRoad = room.getAddressRoad();
        addressDetail = room.getAddressDetail();

        transactions = room.getTransactions().stream()
                .map(RoomTransactionResponse::of)
                .toList();

        postedAt = room.getCreatedDateKST();
    }
}
