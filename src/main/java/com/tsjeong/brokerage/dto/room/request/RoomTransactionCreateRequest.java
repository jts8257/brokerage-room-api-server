package com.tsjeong.brokerage.dto.room.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomTransactionCreateRequest {

    @Positive
    private Integer transactionId;

    @DecimalMax(value = "10000000000000.00", inclusive = false)
    private BigDecimal rentMonthly;
    @DecimalMax(value = "100000000000000000.00", inclusive = false)
    private BigDecimal deposit;

}
