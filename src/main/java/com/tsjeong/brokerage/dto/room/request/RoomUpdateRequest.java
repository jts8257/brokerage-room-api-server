package com.tsjeong.brokerage.dto.room.request;

import com.tsjeong.brokerage.service.util.StringValidator;
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

    private String description;

    @Size(max = 2, min = 1)
    @Valid
    private List<RoomTransactionUpdateRequest> transactions;

    public void setDescription(String description) {
        this.description = StringValidator.isEmptyOrNull(description) ? null : description;
    }

    public void setAddressJibun(String addressJibun) {
        this.addressJibun = StringValidator.isEmptyOrNull(addressJibun) ? null : addressJibun;
    }

    public void setAddressRoad(String addressRoad) {
        this.addressRoad = StringValidator.isEmptyOrNull(addressRoad) ? null : addressRoad;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = StringValidator.isEmptyOrNull(addressDetail) ? null : addressDetail;
    }
}
