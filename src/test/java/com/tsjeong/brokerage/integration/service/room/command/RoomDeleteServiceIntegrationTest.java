package com.tsjeong.brokerage.integration.service.room.command;

import com.tsjeong.brokerage.entity.room.Room;
import com.tsjeong.brokerage.entity.room.RoomTransaction;
import com.tsjeong.brokerage.integration.testconfig.RoomTestBase;
import com.tsjeong.brokerage.repsoitory.room.RoomRepository;
import com.tsjeong.brokerage.repsoitory.room.RoomTransactionRepository;
import com.tsjeong.brokerage.service.room.command.RoomDeleteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoomDeleteServiceIntegrationTest extends RoomTestBase {
    @Autowired
    private RoomDeleteService roomDeleteService;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomTransactionRepository roomTransactionRepository;
    private final BigDecimal deposit = new BigDecimal("1000");

    @Test
    @DisplayName("RoomDeleteService.deleteRoom() - 본인의 방을 지울 수 있다.")
    void shouldDeleteUsersOwnRoom() {
        // Given
        List<RoomSetupConfig> roomConfigs = List.of(
                new RoomSetupConfig(user1, oneRoom, List.of(new RoomTransactionSetupConfig(jeonse, deposit, null))),
                new RoomSetupConfig(user1, oneRoom, List.of(new RoomTransactionSetupConfig(jeonse, deposit, null))),
                new RoomSetupConfig(user2, oneRoom, List.of(new RoomTransactionSetupConfig(jeonse, deposit, null)))
        );
        List<Room> roomsCrated = createRoomsWithTransactions(roomConfigs);
        long user1RoomId = roomsCrated.stream().filter(room -> room.getUser().getId().equals(user1Id)).findFirst().orElseThrow(
                () -> new IllegalStateException("테스트 준비가 제대로 되지 않았습니다.")
        ).getId();

        // When
        roomDeleteService.deleteRoom(user1Id, user1RoomId);

        // Then
        List<Room> roomsFound = roomRepository.findAll();
        assertEquals(2, roomsFound.size());

        long user1RoomCount = roomsFound.stream().filter(room -> room.getUser().getId().equals(user1Id)).count();
        assertEquals(1, user1RoomCount);
    }

    @Test
    @DisplayName("RoomDeleteService.deleteRoom() - 본인의 방을 지웠을때 거래유형도 함께 지워진다.")
    void shouldDeleteUsersOwnRoomWhitTransactionInfo() {
        // Given
        List<RoomSetupConfig> roomConfigs = List.of(
                new RoomSetupConfig(user1, oneRoom, List.of(new RoomTransactionSetupConfig(jeonse, deposit, null))),
                new RoomSetupConfig(user2, oneRoom, List.of(new RoomTransactionSetupConfig(jeonse, deposit, null)))
        );

        List<Room> roomsCrated = createRoomsWithTransactions(roomConfigs);
        long user2RoomId = roomsCrated.stream().filter(room -> room.getUser().getId().equals(user2Id)).findFirst().orElseThrow(
                () -> new IllegalStateException("테스트 준비가 제대로 되지 않았습니다.")
        ).getId();

        // When
        roomDeleteService.deleteRoom(user2Id, user2RoomId);

        // Then
        List<Room> roomsFound = roomRepository.findAll();
        List<RoomTransaction> roomTransactionsFound = roomTransactionRepository.findAll();

        assertEquals(1, roomsFound.size());
        assertEquals(1, roomTransactionsFound.size());

        long user2RoomCount = roomsFound.stream().filter(room -> room.getUser().getId().equals(user2Id)).count();
        assertEquals(0, user2RoomCount);
    }
}
