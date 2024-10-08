package com.tsjeong.brokerage.service.room.command;

import com.tsjeong.brokerage.dto.room.request.RoomTransactionUpdateRequest;
import com.tsjeong.brokerage.entity.room.Room;
import com.tsjeong.brokerage.entity.room.RoomTransaction;
import com.tsjeong.brokerage.entity.room.TransactionType;
import com.tsjeong.brokerage.service.category.TransactionTypeReadService;
import com.tsjeong.brokerage.service.room.validatior.TransactionPriceConditionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomTransactionUpdateService {

    private final TransactionTypeReadService transactionTypeReadService;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<RoomTransaction> updateRoomTransactionBy(
            Room room,
            List<RoomTransactionUpdateRequest> transactionsDtos
    ) {
        List<RoomTransaction> contextLoadedRoomTxs = room.getTransactions();
        List<RoomTransaction> toDelete = new ArrayList<>();

        Set<Integer> roomTxTypeIds = room.getTransactions().stream().map(e->e.getTransactionType().getId()).collect(Collectors.toSet());
        Set<Integer> requestTxTypeIds = transactionsDtos.stream().map(RoomTransactionUpdateRequest::getTransactionId).collect(Collectors.toSet());;

        /////////
        // 삭제 대상 : 기존에 존재하지만 DTO에는 없는 TransactionTypeId
        Set<Integer> transactionTypeToDelete = new HashSet<>(roomTxTypeIds);
        transactionTypeToDelete.removeAll(requestTxTypeIds);

        for (Integer typeToDelete : transactionTypeToDelete) {
            for (RoomTransaction roomTx : contextLoadedRoomTxs) {
                if (typeToDelete.equals(roomTx.getTransactionType().getId())) {
                    toDelete.add(roomTx);
                }
            }
        }
        contextLoadedRoomTxs.removeAll(toDelete);
        // 삭제 대상을 roomTransactionMap 에서 제외시킨다.
        /////////

        Map<Integer, RoomTransactionUpdateRequest> updateRequestMap = transactionsDtos.stream()
                .collect(Collectors.toMap(RoomTransactionUpdateRequest::getTransactionId, request -> request));


        /////////
        // 생성 대상 : DTO에는 존재하지만 기존에는 없는 TransactionTypeId
        Set<Integer> transactionTypeToCreate = new HashSet<>(requestTxTypeIds);
        transactionTypeToCreate.removeAll(roomTxTypeIds);

        for (Integer typeToCreate : transactionTypeToCreate) {
            TransactionType txType = transactionTypeReadService.getTransactionTypeById(typeToCreate);
            contextLoadedRoomTxs.add(RoomTransaction.builder() // 실제 영속성 컨텍스트에 추가
                    .room(room)
                    .rentMonthly(updateRequestMap.get(typeToCreate).getRentMonthly())
                    .deposit(updateRequestMap.get(typeToCreate).getDeposit())
                    .transactionType(txType)
                    .build()
            );

        }
        // 생성 대상을 영속성 컨텍스트에 추가
        /////////

        /////////
        // 수정 대상 : 기존에도 존재하고 DTO에도 존재하는 TransactionTypeId
        for (Integer requestTxTypeId : requestTxTypeIds) {
            for (RoomTransaction roomTx : contextLoadedRoomTxs) {
                Integer contextKey = roomTx.getTransactionType().getId();
                if (contextKey.equals(requestTxTypeId)) {
                    RoomTransactionUpdateRequest updateRequest = updateRequestMap.get(requestTxTypeId);
                    roomTx.update(updateRequest.getRentMonthly(), updateRequest.getDeposit());
                    TransactionPriceConditionValidator.validate(roomTx.getTransactionType(), roomTx.getRentMonthly(), roomTx.getDeposit());
                }
            }
        }

        // 수정 대상의 필드 정보를 수정하고 검증함
        /////////

        return contextLoadedRoomTxs;
    }
}