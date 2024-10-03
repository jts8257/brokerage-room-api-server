package com.tsjeong.brokerage.dto.room.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tsjeong.brokerage.entity.room.RoomTransaction;
import com.tsjeong.brokerage.entity.room.TransactionType;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class RoomTransactionResponse {

    @NotNull @Positive
    private Integer transactionId;
    @NotNull
    private String transactionTypeName;

    @Positive @DecimalMax(value = "10000000000000.00", inclusive = false)
    private BigDecimal rentMonthly;
    @Positive @DecimalMax(value = "100000000000000000.00", inclusive = false)
    private BigDecimal deposit;

    @JsonIgnore
    public static RoomTransactionResponse of(RoomTransaction roomTransaction) {
        RoomTransactionResponse dto = RoomTransactionResponse.of(roomTransaction.getTransactionType());
        dto.setRentMonthly(roomTransaction.getRentMonthly());
        dto.setDeposit(roomTransaction.getDeposit());

        return dto;
    }

    @JsonIgnore
    public static RoomTransactionResponse of(TransactionType transactionType) {
        RoomTransactionResponse dto = new RoomTransactionResponse();
        dto.setTransactionId(transactionType.getId());
        dto.setTransactionTypeName(transactionType.getName());
        return dto;
    }

}
