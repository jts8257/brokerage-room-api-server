package com.tsjeong.brokerage.dto.room.request;

import com.tsjeong.brokerage.service.util.StringValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;

@Data
public class RoomUpdateRequest {
    @Positive
    @Schema(description = "등록 대방 방의 유형 id")
    private Integer roomTypeId;

    @Schema(description = "등록 대방 방의 지번 주소, 도로명 주소와 지번 주소 중 하나는 반드시 필요하다")
    private String addressJibun;
    @Schema(description = "등록 대방 방의 도로명 주소, 도로명 주소와 지번 주소 중 하나는 반드시 필요하다")
    private String addressRoad;
    @Schema(description = "주소 세부 사항", nullable = true)
    private String addressDetail;
    @Schema(description = "추가 사항", nullable = true)
    private String description;

    @Schema(description = "거래 유형과 각 거래유형별 금액, 서로다른 거래 유형으로 1~2개 등록이 가능하다.")
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
