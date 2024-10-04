package com.tsjeong.brokerage.dto.room.response;

import com.tsjeong.brokerage.entity.room.Room;
import com.tsjeong.brokerage.entity.room.RoomType;
import lombok.Data;

import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
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
    private List<RoomTransactionResponse> transactions;

    private ZonedDateTime postedAt;
    private ZonedDateTime updatedAt;

    private String userName;

    // Todo editable, deletable 필드가 추가되어야 하는건 아닐까?

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

        postedAt = room.getCreatedAtKST();
        updatedAt = room.getUpdatedAtKST();
        userName = room.getUser() == null ? null : room.getUser().getNickName();
    }
}
