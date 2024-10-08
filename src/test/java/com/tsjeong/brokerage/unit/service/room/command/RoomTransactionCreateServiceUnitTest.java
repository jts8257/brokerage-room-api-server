package com.tsjeong.brokerage.unit.service.room.command;

import com.tsjeong.brokerage.dto.room.request.RoomTransactionCreateRequest;
import com.tsjeong.brokerage.entity.room.Room;
import com.tsjeong.brokerage.entity.room.RoomTransaction;
import com.tsjeong.brokerage.entity.room.TransactionType;
import com.tsjeong.brokerage.exception.ApplicationException;
import com.tsjeong.brokerage.repsoitory.room.RoomTransactionRepository;
import com.tsjeong.brokerage.service.category.TransactionTypeReadService;
import com.tsjeong.brokerage.service.room.command.RoomTransactionCreateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class RoomTransactionCreateServiceUnitTest {

    @InjectMocks
    private RoomTransactionCreateService roomTransactionCreateService;
    @Mock
    private TransactionTypeReadService transactionTypeReadService;
    @Mock
    private RoomTransactionRepository roomTransactionRepository;

    private final Room room = Room.builder().id(1L).build();
    private RoomTransactionCreateRequest requestJeonse1;
    private RoomTransactionCreateRequest requestJeonse2;
    private RoomTransactionCreateRequest requestRent1;
    private RoomTransactionCreateRequest requestRent2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        requestJeonse1 = new RoomTransactionCreateRequest();
        requestJeonse1.setTransactionId(1);
        requestJeonse1.setDeposit(new BigDecimal("150000000"));

        requestJeonse2 = new RoomTransactionCreateRequest();
        requestJeonse2.setTransactionId(1);
        requestJeonse2.setDeposit(new BigDecimal("200000000"));

        requestRent1 = new RoomTransactionCreateRequest();
        requestRent1.setTransactionId(2);
        requestRent1.setRentMonthly(new BigDecimal("200000")); // rentMonthly 설정
        requestRent1.setDeposit(new BigDecimal("20000000"));

        requestRent2 = new RoomTransactionCreateRequest();
        requestRent2.setTransactionId(2);
        requestRent2.setRentMonthly(new BigDecimal("30000000"));
        requestRent2.setDeposit(new BigDecimal("15000000"));

        when(transactionTypeReadService.getTransactionTypeById(1))
                .thenReturn(TransactionType.builder().id(1).isDepositOnly(true).name("전세").build());

        when(transactionTypeReadService.getTransactionTypeById(2))
                .thenReturn(TransactionType.builder().id(2).isDepositOnly(false).name("월세").build());

        when(roomTransactionRepository.saveAll(anyList()))
                .thenAnswer(invocation -> invocation.getArgument(0));

    }

    @Test
    @DisplayName("RoomTransactionCreateService.createRoomTransactionBy() - 성공적으로 전세 거래 유형 저장")
    void shouldReturnValidJeonseEntities() {
        // Given
        int transactionId = requestJeonse1.getTransactionId();
        List<RoomTransactionCreateRequest> transactions = List.of(requestJeonse1);

        // When
        List<RoomTransaction> roomTransactions = roomTransactionCreateService.createRoomTransactionBy(room, transactions);

        // Then
        assertFalse(roomTransactions.isEmpty());
        assertEquals(roomTransactions.size() ,1);

        RoomTransaction rt = roomTransactions.get(0);
        assertEquals(transactionId, rt.getTransactionType().getId());
        assertEquals(requestJeonse1.getDeposit(), rt.getDeposit());
        assertNull(rt.getRentMonthly());

        verify(transactionTypeReadService, times(1)).getTransactionTypeById(transactionId);
        verify(roomTransactionRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("RoomTransactionCreateService.createRoomTransactionBy() - 성공적으로 임대 거래 유형 저장")
    void shouldReturnValidRentEntities() {
        // Given
        int transactionId = requestRent1.getTransactionId();
        List<RoomTransactionCreateRequest> transactions = List.of(requestRent1);

        // When
        List<RoomTransaction> roomTransactions = roomTransactionCreateService.createRoomTransactionBy(room, transactions);

        // Then
        assertFalse(roomTransactions.isEmpty());
        assertEquals(roomTransactions.size() ,1);

        RoomTransaction rt = roomTransactions.get(0);
        assertEquals(transactionId, rt.getTransactionType().getId());
        assertEquals(requestRent1.getDeposit(), rt.getDeposit());
        assertEquals(requestRent1.getRentMonthly(), rt.getRentMonthly());

        verify(transactionTypeReadService, times(1)).getTransactionTypeById(transactionId);
        verify(roomTransactionRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("RoomTransactionCreateService.createRoomTransactionBy() - 동일한 거래 유형(전세)을 2개 이상 등록하려는 경우 예외 발생")
    void shouldThrowExceptionWhenHaveSameTransactionId1() {
        // Given
        int transactionId = requestJeonse1.getTransactionId();
        List<RoomTransactionCreateRequest> transactions = List.of(requestJeonse1, requestJeonse2);

        // When & Then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> roomTransactionCreateService.createRoomTransactionBy(room, transactions));


        assertEquals(exception.getMessage(), "동일한 거래 유형 '전세'를 2개 이상 등록할 수 없습니다.");
        verify(transactionTypeReadService, times(1)).getTransactionTypeById(transactionId);
        verify(roomTransactionRepository, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("RoomTransactionCreateService.createRoomTransactionBy() - 동일한 거래 유형(임대)을 2개 이상 등록하려는 경우 예외 발생")
    void shouldThrowExceptionWhenHaveSameTransactionId2() {
        // Given
        int transactionId = requestRent1.getTransactionId();
        List<RoomTransactionCreateRequest> transactions = List.of(requestRent1, requestRent2);

        // When & Then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> roomTransactionCreateService.createRoomTransactionBy(room, transactions));


        assertEquals(exception.getMessage(), "동일한 거래 유형 '월세'를 2개 이상 등록할 수 없습니다.");
        verify(transactionTypeReadService, times(1)).getTransactionTypeById(transactionId);
        verify(roomTransactionRepository, never()).saveAll(anyList());
    }


    @Test
    @DisplayName("RoomTransactionCreateService.createRoomTransactionBy() - 거래 유형 리스트가 비어있는 경우는 DTO 에서 처리하므로 Exception 이 발생하지 않는다.")
    void shouldNOTThrowExceptionWhenTransactionsEmpty() {
        // Given
        List<RoomTransactionCreateRequest> transactions = List.of();

        // When
        List<RoomTransaction> roomTransactions = roomTransactionCreateService.createRoomTransactionBy(room, transactions);

        // Then
        assertTrue(roomTransactions.isEmpty());
        verify(transactionTypeReadService, never()).getTransactionTypeById(anyInt());
        verify(roomTransactionRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("RoomTransactionCreateService.createRoomTransactionBy() - 월세에 임대료 누락시 예외 발생")
    void shouldThrowExceptionWhenRentHaveNoRentMonthly() {
        // Given
        requestRent1.setRentMonthly(null);
        List<RoomTransactionCreateRequest> transactions = List.of(requestRent1);

        // When & Then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> roomTransactionCreateService.createRoomTransactionBy(room, transactions));

        assertEquals(exception.getMessage(), "거래유형 '월세'에 대한 임대료가 누락되었습니다.");
        verify(transactionTypeReadService, times(1)).getTransactionTypeById(2);
        verify(roomTransactionRepository, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("RoomTransactionCreateService.createRoomTransactionBy() - 전세에 보증금 누락시 예외 발생")
    void shouldThrowExceptionWhenJeonseHaveNoDeposit() {
        // Given
        requestJeonse1.setDeposit(null);
        requestJeonse1.setRentMonthly(new BigDecimal(1000000));
        int transactionId = requestJeonse1.getTransactionId();
        List<RoomTransactionCreateRequest> transactions = List.of(requestJeonse1);

        // When & Then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> roomTransactionCreateService.createRoomTransactionBy(room, transactions));

        assertEquals(exception.getMessage(), "거래유형 '전세'에 대한 보증금이 누락되었습니다.");
        verify(transactionTypeReadService, times(1)).getTransactionTypeById(transactionId);
        verify(roomTransactionRepository, never()).saveAll(anyList());
    }
}
