package com.tsjeong.brokerage.service.category;

import com.tsjeong.brokerage.entity.room.TransactionType;

import java.util.List;

public interface TransactionTypeReadService {
    TransactionType getTransactionTypeById(Integer transactionTypeId);

    List<TransactionType> getAllTransactionTypes();
}
