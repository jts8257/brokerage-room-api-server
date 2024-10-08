package com.tsjeong.brokerage.integration.testconfig;

import com.tsjeong.brokerage.dto.room.request.RoomTransactionCreateRequest;
import com.tsjeong.brokerage.entity.room.Room;
import com.tsjeong.brokerage.entity.room.RoomType;
import com.tsjeong.brokerage.entity.room.TransactionType;
import com.tsjeong.brokerage.entity.user.Users;
import com.tsjeong.brokerage.repsoitory.room.RoomRepository;
import com.tsjeong.brokerage.repsoitory.room.RoomTransactionRepository;
import com.tsjeong.brokerage.service.room.command.RoomCreateService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RoomTestBase extends IntegrationTestBase {

    @Autowired
    protected RoomRepository roomRepository;
    @Autowired
    protected RoomTransactionRepository transactionRepository;

    @Autowired
    private RoomCreateService roomCreateService;

    protected List<Room> createRoomsWithTransactions(List<RoomSetupConfig> roomSetupConfigs) {

        List<Room> roomsCreated = new ArrayList<>();
        for (RoomSetupConfig roomSetup : roomSetupConfigs) {
            List<RoomTransactionCreateRequest> txDtos = roomSetup.transactionSetupConfigs.stream()
                    .map(txSetUp -> new RoomTransactionCreateRequest(
                            txSetUp.transactionType.getId(),
                            txSetUp.rentMonthly,
                            txSetUp.deposit))
                    .toList();

            Room roomCreated = roomCreateService.createRoom(roomSetup.user.getId(),
                    roomSetup.roomType.getId(),
                    "addressJibun",
                    "addressRoad",
                    "addressDetail",
                    null,
                    txDtos);

            roomsCreated.add(roomCreated);
        }

        return roomsCreated;

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