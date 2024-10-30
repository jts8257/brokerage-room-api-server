package com.tsjeong.brokerage.dto.room.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomTransactionCreateRequest {

    @Positive
    @Schema(description = "거래 유형 id")
    private Integer transactionId;

    @Schema(description = "월세 금액(원), 전세인 경우 값이 없어야 한다.", nullable = true, maximum = "10000000000000.00")
    @DecimalMax(value = "10000000000000.00", inclusive = false)
    private BigDecimal rentMonthly;

    @Schema(description = "보증 금액(원), 전세인 경우 값이 반드시 필요하며, 월세인 경우 없을 수 있다.", nullable = true,
            maximum = "100000000000000000.00")
    @DecimalMax(value = "100000000000000000.00", inclusive = false)
    private BigDecimal deposit;

    public void setRentMonthly(BigDecimal rentMonthly) {
        this.rentMonthly = (rentMonthly != null && rentMonthly.signum() == 0) ? null : rentMonthly;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = (deposit != null && deposit.signum() == 0) ? null : deposit;
    }
}
