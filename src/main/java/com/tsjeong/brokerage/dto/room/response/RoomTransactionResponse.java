package com.tsjeong.brokerage.dto.room.response;


import java.math.BigDecimal;

public record RoomTransactionResponse(
        Integer transactionId,
        String transactionTypeName,
        BigDecimal rentMonthly,
        BigDecimal deposit
) { }
