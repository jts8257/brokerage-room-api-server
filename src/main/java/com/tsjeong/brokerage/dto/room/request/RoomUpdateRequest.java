package com.tsjeong.brokerage.dto.room.request;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class RoomUpdateRequest {
    @Positive
    private Integer roomTypeId;

    private String addressJibun;
    private String addressRoad;
    private String addressDetail;

    @Size(max = 2, min = 1)
    @Valid
    private List<RoomTransactionUpdateRequest> transactions;
}
