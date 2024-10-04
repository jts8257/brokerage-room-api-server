package com.tsjeong.brokerage.service.room.command;

import com.tsjeong.brokerage.dto.room.request.RoomTransactionCreateRequest;
import com.tsjeong.brokerage.entity.room.Room;
import com.tsjeong.brokerage.entity.room.RoomTransaction;
import com.tsjeong.brokerage.entity.room.TransactionType;
import com.tsjeong.brokerage.exception.ErrorCode;
import com.tsjeong.brokerage.repsoitory.room.RoomTransactionRepository;
import com.tsjeong.brokerage.service.room.query.TransactionTypeReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomTransactionCreateService {

    private final TransactionTypeReadService transactionTypeReadService;
    private final RoomTransactionRepository roomTransactionRepository;

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
            throw ErrorCode.ENTITY_CONFLICT.build("동일한 거래 유형 '%s'를 2개 이상 등록할 수 없습니다.".formatted(type.getName()));
        }

        RoomTransactionCreateRequest dto = dtoList.get(0);
        validateTransactionPriceCondition(type, dto);

        return RoomTransaction.builder()
                .transactionType(type)
                .room(room)
                .rentMonthly(dto.getRentMonthly())
                .deposit(dto.getDeposit())
                .build();
    }

    private void validateTransactionPriceCondition(TransactionType type, RoomTransactionCreateRequest dto) {
        if (Objects.isNull(dto.getRentMonthly()) && Objects.isNull(dto.getDeposit())) {
            throw ErrorCode.ENTITY_CONFLICT
                    .build("거래유형 '%s'에 대한 임대료 와 보증금이 모두 누락되었습니다.".formatted(type.getName()));
        }

        if (!type.getIsDepositOnly() && (dto.getRentMonthly() == null || dto.getRentMonthly().signum() == 0)) {
            throw ErrorCode.ENTITY_CONFLICT.build("거래유형 '%s'에 대한 임대료가 누락되었습니다.".formatted(type.getName()));
        }

        if (type.getIsDepositOnly()) {
            if (dto.getDeposit() == null || dto.getDeposit().signum() == 0) {
                throw ErrorCode.ENTITY_CONFLICT.build("거래유형 '%s'에 대한 보증금이 누락되었습니다.".formatted(type.getName()));
            }
            if (dto.getRentMonthly() != null) {
                throw ErrorCode.ENTITY_CONFLICT.build("거래유형 '%s'에 대한 임대료를 지정할 수 없지만 임대료 '%s'를 부여하려고 합니다."
                                .formatted(type.getName(), dto.getRentMonthly()));
            }
        }
    }
}
