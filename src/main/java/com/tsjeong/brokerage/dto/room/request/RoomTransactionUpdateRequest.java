package com.tsjeong.brokerage.dto.room.request;

import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class RoomTransactionUpdateRequest {

    @Positive
    private Integer transactionId;

    @Positive @DecimalMax(value = "10000000000000.00", inclusive = false)
    private BigDecimal rentMonthly;
    @Positive @DecimalMax(value = "100000000000000000.00", inclusive = false)
    private BigDecimal deposit;

    public void setRentMonthly(BigDecimal rentMonthly) {
        this.rentMonthly = (rentMonthly != null && rentMonthly.signum() == 0) ? null : rentMonthly;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = (deposit != null && deposit.signum() == 0) ? null : deposit;
    }
}
