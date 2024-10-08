package com.tsjeong.brokerage.dto.room.mapper;

import com.tsjeong.brokerage.dto.room.response.RoomTransactionResponse;
import com.tsjeong.brokerage.entity.room.RoomTransaction;
import com.tsjeong.brokerage.entity.room.TransactionType;

public class RoomTransactionMapper {
    public static RoomTransactionResponse toResponse(RoomTransaction roomTransaction) {
        TransactionType transactionType = roomTransaction.getTransactionType();

        return new RoomTransactionResponse(
                transactionType.getId(),
                transactionType.getName(),
                roomTransaction.getRentMonthly(),
                roomTransaction.getDeposit()
        );
    }
}
