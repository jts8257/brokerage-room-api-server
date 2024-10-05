package com.tsjeong.brokerage.integration.testconfig;

import com.tsjeong.brokerage.dto.room.request.RoomTransactionCreateRequest;
import com.tsjeong.brokerage.entity.room.RoomType;
import com.tsjeong.brokerage.entity.room.TransactionType;
import com.tsjeong.brokerage.entity.user.Users;
import com.tsjeong.brokerage.repsoitory.room.RoomRepository;
import com.tsjeong.brokerage.repsoitory.room.RoomTransactionRepository;
import com.tsjeong.brokerage.service.room.command.RoomCreateService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

public class RoomQueryTestBase extends IntegrationTestBase {

    @Autowired
    protected RoomRepository roomRepository;
    @Autowired
    protected RoomTransactionRepository transactionRepository;

    @Autowired
    private RoomCreateService roomCreateService;

    protected void createRoomsWithTransactions(List<RoomSetupConfig> roomSetupConfigs) {

        for (RoomSetupConfig roomSetup : roomSetupConfigs) {
            List<RoomTransactionCreateRequest> txDtos = roomSetup.transactionSetupConfigs.stream()
                    .map(txSetUp -> new RoomTransactionCreateRequest(
                            txSetUp.transactionType.getId(),
                            txSetUp.rentMonthly,
                            txSetUp.deposit))
                    .toList();

            roomCreateService.createRoom(roomSetup.user.getId(),
                    roomSetup.roomType.getId(),
                    "addressJibun",
                    "addressRoad",
                    "addressDetail",
                    null,
                    txDtos);

        }

    }

    public record RoomSetupConfig(
            Users user,
            RoomType roomType,
            List<RoomTransactionSetupConfig> transactionSetupConfigs
    ) { }

    public record RoomTransactionSetupConfig(
            TransactionType transactionType,
            BigDecimal deposit,
            BigDecimal rentMonthly
    ) {}
}