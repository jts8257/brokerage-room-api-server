package com.tsjeong.brokerage.dto.room.mapper;

import com.tsjeong.brokerage.dto.room.response.RoomTransactionResponse;
import com.tsjeong.brokerage.entity.room.RoomTransaction;
import com.tsjeong.brokerage.entity.room.TransactionType;
import com.tsjeong.brokerage.exception.ErrorCode;

import java.util.Objects;

class RoomTransactionMapper {
    static RoomTransactionResponse toResponse(RoomTransaction roomTransaction) {
        TransactionType transactionType = roomTransaction.getTransactionType();

        if (Objects.isNull(transactionType)) {
            throw ErrorCode.MISSING_REQUIRED_RELATION.build("방과 매핑된 거래유형에서 '거래유형' 정보가 누락되었습니다.");
        }

        return new RoomTransactionResponse(
                transactionType.getId(),
                transactionType.getName(),
                roomTransaction.getRentMonthly(),
                roomTransaction.getDeposit()
        );
    }
}
