package com.tsjeong.brokerage.integration.service.room.command;

import com.tsjeong.brokerage.dto.room.request.RoomTransactionUpdateRequest;
import com.tsjeong.brokerage.entity.room.Room;
import com.tsjeong.brokerage.entity.room.RoomTransaction;
import com.tsjeong.brokerage.integration.testconfig.RoomTestBase;
import com.tsjeong.brokerage.repsoitory.room.RoomRepository;
import com.tsjeong.brokerage.service.room.command.RoomUpdateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoomUpdateServiceIntegrationTest extends RoomTestBase {
    @Autowired
    private RoomUpdateService roomUpdateService;
    @Autowired
    private RoomRepository roomRepository;
    private final BigDecimal depositFrom = new BigDecimal("1000");
    private final BigDecimal depositTo = new BigDecimal("10000");
    private final BigDecimal rentFrom = new BigDecimal("50");
    private final BigDecimal rentTo = new BigDecimal("100");

    private RoomTransactionUpdateRequest jeonseRequest;
    private RoomTransactionUpdateRequest rentRequest;

    @BeforeEach
    public void setUp() {
        super.setUp();
        jeonseRequest = new RoomTransactionUpdateRequest();
        jeonseRequest.setTransactionId(jeonseId);
        jeonseRequest.setRentMonthly(null);
        jeonseRequest.setDeposit(depositTo);

        rentRequest = new RoomTransactionUpdateRequest();
        rentRequest.setTransactionId(rentId);
        rentRequest.setRentMonthly(rentTo);
        rentRequest.setDeposit(depositTo);
    }
    @Test
    @DisplayName("RoomUpdateService.roomUpdate() - Room 의 정보가 성공적으로 수정된다")
    void shouldUpdateRoomInfo() {
        // Given
        BigDecimal deposit = new BigDecimal("1000");
        List<RoomSetupConfig> roomConfigs = List.of(
                new RoomSetupConfig(user1, oneRoom, List.of(new RoomTransactionSetupConfig(jeonse, deposit, null))));
        Room roomCreated = createRoomsWithTransactions(roomConfigs).stream()
                .findFirst().orElseThrow(() -> new IllegalStateException("테스트 준비가 제대로 되지 않았습니다."));

        long roomId = roomCreated.getId();
        int roomTypeId = twoRoom.getId();
        List<RoomTransactionUpdateRequest> transactionUpdateRequestList = List.of(jeonseRequest);

        String addressJibunToUpdate = "addressJibun2";
        String addressRoadToUpdate = "addressRoad2";
        String addressDetailToUpdate = "addressDetail2";
        String descriptionToUpdate = "description2";

        // When
        Room roomUpdated = roomUpdateService.roomUpdate(user1Id, roomId, roomTypeId,
                addressJibunToUpdate, addressRoadToUpdate, addressDetailToUpdate, descriptionToUpdate,
                transactionUpdateRequestList
        );

        // Then
        assertEquals(addressJibunToUpdate, roomUpdated.getAddressJibun());
        assertEquals(addressRoadToUpdate, roomUpdated.getAddressRoad());
        assertEquals(addressDetailToUpdate, roomUpdated.getAddressDetail());
        assertEquals(descriptionToUpdate, roomUpdated.getDetail().getDescription());
        assertEquals(twoRoom, roomUpdated.getRoomType());
    }

    @Test
    @DisplayName("RoomUpdateService.roomUpdate() - Room 의 거래유형, 가격 정보가 성공적으로 수정된다")
    void shouldUpdateRoomTransaction() {

        List<RoomSetupConfig> roomConfigs = List.of(new RoomSetupConfig(user1, oneRoom,
                List.of(
                        new RoomTransactionSetupConfig(jeonse, depositFrom, null),
                        new RoomTransactionSetupConfig(rent, depositFrom, rentFrom))
                ));
        Room roomCreated = createRoomsWithTransactions(roomConfigs).stream().findFirst().orElseThrow(
                () -> new IllegalStateException("테스트 준비가 제대로 되지 않았습니다."));

        long roomId = roomCreated.getId();
        int roomTypeId = roomCreated.getRoomType().getId();
        List<RoomTransactionUpdateRequest> transactionUpdateRequestList = List.of(jeonseRequest, rentRequest);

        // When
        Room roomUpdated = roomUpdateService.roomUpdate(user1Id, roomId, roomTypeId,
                roomCreated.getAddressJibun(), roomCreated.getAddressRoad(), roomCreated.getAddressDetail(),
                roomCreated.getDetail().getDescription(), transactionUpdateRequestList);

        // Then
        List<RoomTransaction> roomTransactionsFound = roomUpdated.getTransactions();
        assertEquals(2, roomTransactionsFound.size());

        RoomTransaction roomTxJeonse = roomTransactionsFound.stream()
                .filter(roomTx -> roomTx.getTransactionType().equals(jeonse))
                .findFirst().orElseThrow(() -> new IllegalStateException("내방 업데이트 이후 전세 거래 유형을 찾지 못했습니다."));

        RoomTransaction roomTxRent = roomTransactionsFound.stream()
                .filter(roomTx -> roomTx.getTransactionType().equals(rent))
                .findFirst().orElseThrow(() -> new IllegalStateException("내방 업데이트 이후 월세 거래 유형을 찾지 못했습니다."));

        assertEquals(depositTo, roomTxJeonse.getDeposit());
        assertEquals(depositTo, roomTxRent.getDeposit());
        assertEquals(rentTo, roomTxRent.getRentMonthly());
    }

    @Test
    @DisplayName("RoomUpdateService.roomUpdate() - Room 의 거래유형 정보가 성공적으로 추가된다.")
    void shouldAppendRoomTransaction() {
        List<RoomSetupConfig> roomConfigs = List.of(
                new RoomSetupConfig(user1, oneRoom, List.of(new RoomTransactionSetupConfig(jeonse, depositFrom, null))
        ));
        Room roomCreated = createRoomsWithTransactions(roomConfigs).stream().findFirst().orElseThrow(
                () -> new IllegalStateException("테스트 준비가 제대로 되지 않았습니다."));

        long roomId = roomCreated.getId();
        int roomTypeId = roomCreated.getRoomType().getId();
        List<RoomTransactionUpdateRequest> transactionUpdateRequestList = List.of(jeonseRequest, rentRequest);

        // When
        Room roomUpdated = roomUpdateService.roomUpdate(user1Id, roomId, roomTypeId,
                roomCreated.getAddressJibun(), roomCreated.getAddressRoad(), roomCreated.getAddressDetail(),
                roomCreated.getDetail().getDescription(), transactionUpdateRequestList);

        // Then
        List<RoomTransaction> roomTransactionsFound = roomUpdated.getTransactions();
        assertEquals(2, roomTransactionsFound.size());

        RoomTransaction roomTxJeonse = roomTransactionsFound.stream()
                .filter(roomTx -> roomTx.getTransactionType().equals(jeonse))
                .findFirst().orElseThrow(() -> new IllegalStateException("내방 업데이트 이후 전세 거래 유형을 찾지 못했습니다."));

        RoomTransaction roomTxRent = roomTransactionsFound.stream()
                .filter(roomTx -> roomTx.getTransactionType().equals(rent))
                .findFirst().orElseThrow(() -> new IllegalStateException("내방 업데이트 이후 월세 거래 유형을 찾지 못했습니다."));

        assertEquals(depositTo, roomTxJeonse.getDeposit());
        assertEquals(depositTo, roomTxRent.getDeposit());
        assertEquals(rentTo, roomTxRent.getRentMonthly());
    }

    @Test
    @DisplayName("RoomUpdateService.roomUpdate() - Room 의 거래유형 정보가 성공적으로 삭제된다.")
    void shouldDeleteRoomTransaction() {
        List<RoomSetupConfig> roomConfigs = List.of(new RoomSetupConfig(user1, oneRoom,
                List.of(
                        new RoomTransactionSetupConfig(jeonse, depositFrom, null),
                        new RoomTransactionSetupConfig(rent, depositFrom, rentFrom))
        ));
        Room roomCreated = createRoomsWithTransactions(roomConfigs).stream().findFirst().orElseThrow(
                () -> new IllegalStateException("테스트 준비가 제대로 되지 않았습니다."));

        long roomId = roomCreated.getId();
        int roomTypeId = roomCreated.getRoomType().getId();
        List<RoomTransactionUpdateRequest> transactionUpdateRequestList = List.of( rentRequest);

        // When
        Room roomUpdated = roomUpdateService.roomUpdate(user1Id, roomId, roomTypeId,
                roomCreated.getAddressJibun(), roomCreated.getAddressRoad(), roomCreated.getAddressDetail(),
                roomCreated.getDetail().getDescription(), transactionUpdateRequestList);

        // Then
        List<RoomTransaction> roomTransactionsFound = roomUpdated.getTransactions();
        assertEquals(1, roomTransactionsFound.size());

        RoomTransaction roomTxRent = roomTransactionsFound.stream()
                .filter(roomTx -> roomTx.getTransactionType().equals(rent))
                .findFirst().orElseThrow(() -> new IllegalStateException("내방 업데이트 이후 월세 거래 유형을 찾지 못했습니다."));

        assertEquals(depositTo, roomTxRent.getDeposit());
        assertEquals(rentTo, roomTxRent.getRentMonthly());
    }
}
