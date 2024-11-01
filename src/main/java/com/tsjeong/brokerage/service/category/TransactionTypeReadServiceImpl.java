package com.tsjeong.brokerage.service.category;

import com.tsjeong.brokerage.entity.room.TransactionType;
import com.tsjeong.brokerage.exception.ErrorCode;
import com.tsjeong.brokerage.repsoitory.room.TransactionTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionTypeReadServiceImpl implements TransactionTypeReadService {
    private final TransactionTypeRepository transactionTypeRepository;

    public TransactionType getTransactionTypeById(Integer transactionTypeId) {
        return transactionTypeRepository.findById(transactionTypeId).orElseThrow(
                () -> ErrorCode.ENTITY_NOT_FOUND.build("'id:%d'인 거래 유형을 찾을 수 없습니다."));
    }

    public List<TransactionType> getAllTransactionTypes() {
        return transactionTypeRepository.findAll();
    }
}
