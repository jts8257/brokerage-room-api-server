package com.tsjeong.brokerage.service.room.command;

import com.tsjeong.brokerage.dto.room.request.RoomTransactionCreateRequest;
import com.tsjeong.brokerage.entity.room.Room;
import com.tsjeong.brokerage.entity.room.RoomTransaction;
import com.tsjeong.brokerage.entity.room.TransactionType;
import com.tsjeong.brokerage.exception.ErrorCode;
import com.tsjeong.brokerage.repsoitory.room.RoomTransactionRepository;
import com.tsjeong.brokerage.service.category.TransactionTypeReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomTransactionCreateService {

    private final TransactionTypeReadService transactionTypeReadService;
    private final RoomTransactionRepository roomTransactionRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<RoomTransaction> createRoomTransactionBy(Room room, List<RoomTransactionCreateRequest> transactions) {
        return roomTransactionRepository.saveAll(
                transactions.stream()
                .collect(Collectors.groupingBy(RoomTransactionCreateRequest::getTransactionId))
                .entrySet().stream()
                .map(entry -> createRoomTransaction(room, entry))
                .collect(Collectors.toList())
        );
    }

    private RoomTransaction createRoomTransaction(
            Room room,
            Map.Entry<Integer, List<RoomTransactionCreateRequest>> entry
    ) {
        Integer typeId = entry.getKey();
        List<RoomTransactionCreateRequest> dtoList = entry.getValue();

        TransactionType type = transactionTypeReadService.getTransactionTypeById(typeId);

        if (dtoList.size() > 1) {
            throw ErrorCode.ENTITY_REGISTER_CONFLICT.build("동일한 거래 유형 '%s'를 2개 이상 등록할 수 없습니다.".formatted(type.getName()));
        }

        RoomTransactionCreateRequest dto = dtoList.get(0);

        TransactionPriceConditionValidator.validate(type, dto.getRentMonthly(), dto.getDeposit());

        return RoomTransaction.builder()
                .transactionType(type)
                .room(room)
                .rentMonthly(dto.getRentMonthly())
                .deposit(dto.getDeposit())
                .build();
    }

}
