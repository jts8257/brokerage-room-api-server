package com.tsjeong.brokerage.integration.service.room.command;

import com.tsjeong.brokerage.dto.room.request.RoomTransactionCreateRequest;
import com.tsjeong.brokerage.entity.room.Room;
import com.tsjeong.brokerage.entity.room.RoomTransaction;
import com.tsjeong.brokerage.entity.user.Users;
import com.tsjeong.brokerage.integration.IntegrationTestBase;
import com.tsjeong.brokerage.repsoitory.room.RoomRepository;
import com.tsjeong.brokerage.repsoitory.room.RoomTransactionRepository;
import com.tsjeong.brokerage.service.room.command.RoomCreateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoomCreateServiceIntegrationTest extends IntegrationTestBase {
    @Autowired
    private RoomCreateService roomCreateService;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomTransactionRepository roomTransactionRepository;


    @Test
    @DisplayName("createRoom - 성공적으로 Room과 RoomTransaction이 저장됨")
    void shouldCreateRoomAndTransactionsSuccessfully() {
        // Given
        long userId = user1Id;
        int roomTypeId = oneRoomId;
        String addressJibun = "address jibun input!--1번지 산";
        String addressRoad = "address road input!-124길 길";
        String addressDetail = "address detail input!%^&";
        String roomDetailDescription = "room detail description input!@!";
        RoomTransactionCreateRequest transaction1 = new RoomTransactionCreateRequest();
        RoomTransactionCreateRequest transaction2 = new RoomTransactionCreateRequest();

        transaction1.setTransactionId(jeonseId);
        transaction1.setRentMonthly(null);
        transaction1.setDeposit(new BigDecimal("1500000000"));

        transaction2.setTransactionId(rentId);
        transaction2.setRentMonthly(new BigDecimal("600000"));
        transaction2.setDeposit(new BigDecimal("10000000"));

        // When
        Room roomCreated = roomCreateService.createRoom(
                userId, roomTypeId, addressJibun, addressRoad, addressDetail, roomDetailDescription, List.of(transaction1, transaction2));

        // Then
        Room roomFound = roomRepository.findAll().get(0);
        Users userAssigned = roomCreated.getUser();
        Users userFound = usersRepository.findById(user1Id).orElse(null);

        List<RoomTransaction> roomTransactionsCreated = roomCreated.getTransactions();
        List<RoomTransaction> roomTransactionsFound = roomTransactionRepository.findAll();

        assertEquals(userFound, userAssigned);
        assertEquals(roomFound, roomCreated);
        assertEquals(roomTransactionsFound, roomTransactionsCreated);

        assertEquals(roomFound.getRoomType(), roomCreated.getRoomType());
        assertEquals(roomFound.getAddressJibun(), roomCreated.getAddressJibun());
        assertEquals(roomFound.getAddressRoad(), roomCreated.getAddressRoad());
        assertEquals(roomFound.getAddressDetail(), roomCreated.getAddressDetail());

        assertNotNull(roomCreated.getDetail());
        assertNotNull(roomCreated.getDetail().getId());
        assertEquals(roomDetailDescription, roomCreated.getDetail().getDescription());
    }
}
