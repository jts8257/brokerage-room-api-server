package com.tsjeong.brokerage.unit.service.room.query;

import com.tsjeong.brokerage.dto.room.response.RoomAbbrResponse;
import com.tsjeong.brokerage.entity.room.Room;
import com.tsjeong.brokerage.entity.room.RoomTransaction;
import com.tsjeong.brokerage.entity.room.RoomType;
import com.tsjeong.brokerage.entity.room.TransactionType;
import com.tsjeong.brokerage.entity.user.Users;
import com.tsjeong.brokerage.repsoitory.room.RoomPaginationRepository;
import com.tsjeong.brokerage.service.category.RoomTypeReadService;
import com.tsjeong.brokerage.service.category.TransactionTypeReadService;
import com.tsjeong.brokerage.service.room.query.RoomQueryPageService;
import com.tsjeong.brokerage.service.room.query.enums.RoomQueryMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class RoomQueryPageServiceUnitTest {

    @InjectMocks
    private RoomQueryPageService roomQueryPageService;
    @Mock
    private RoomPaginationRepository roomRepository;
    @Mock
    private RoomTypeReadService roomTypeReadService;
    @Mock
    private TransactionTypeReadService transactionTypeReadService;

    private final RoomType roomType1 = RoomType.builder().id(1).name("roomType1").build();
    private final RoomType roomType2 = RoomType.builder().id(2).name("roomType2").build();
    private final TransactionType txType1 = TransactionType.builder().id(1).name("txType1").isDepositOnly(true).build();
    private final TransactionType txType2 = TransactionType.builder().id(1).name("txType2").isDepositOnly(false).build();
    private final RoomQueryMode mode = RoomQueryMode.ALL;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(roomTypeReadService.getAllRoomTypes()).thenReturn(
                List.of(roomType1, roomType2));

        when(transactionTypeReadService.getAllTransactionTypes()).thenReturn(
                List.of(txType1, txType2));
    }

    @Test
    @DisplayName("RoomQueryPageService.getRoomsPageBy() - 유저가 매핑되지 않은 결과가 있어도 결과에서만 제외될뿐 예외는 발생하지 않는다.")
    void shouldNotThrowsExceptionWhenPagingRoomWithNoUserMappedResult() {
        // Given
        Users users = Users.builder().id(1L).nickName("Mock User Name").build();
        when(roomRepository.findAllRoomBy(
                anyLong(), anyInt(), anyList(), anyList(), isNull(), isNull(), isNull(), isNull()))
                .thenReturn(List.of(
                        Room.builder().id(1L).roomType(roomType1).user(users)
                                .transactions(List.of(RoomTransaction.builder().transactionType(txType1).build()))
                                .build(),
                        Room.builder().id(2L).roomType(roomType1).user(users)
                                .transactions(List.of(RoomTransaction.builder().transactionType(txType1).build()))
                                .build(),
                        Room.builder().id(3L).roomType(roomType1).user(users)
                                .transactions(List.of(RoomTransaction.builder().transactionType(txType1).build()))
                                .build(),
                        Room.builder().id(4L).roomType(roomType1).user(users)
                                .transactions(List.of(RoomTransaction.builder().transactionType(txType1).build()))
                                .build(),
                        Room.builder().id(5L).roomType(roomType1)
                                .transactions(List.of(RoomTransaction.builder().transactionType(txType1).build()))
                                .build()
                ));

        // When
        List<RoomAbbrResponse> result = roomQueryPageService
                .getRoomsPageBy(1L, 10000L, 10, List.of(), List.of(),
                        null, null, null, null, mode);

        // Then
        assertEquals(4, result.size());
    }

    @Test
    @DisplayName("RoomQueryPageService.getRoomsPageBy() - 방 유형이 매핑되지 않은 결과가 있어도 결과에서만 제외될뿐 예외는 발생하지 않는다.")
    void shouldNotThrowsExceptionWhenPagingRoomWithNoRooMTypeMappedResult() {
        // Given
        Users users = Users.builder().id(1L).nickName("Mock User Name").build();
        when(roomRepository.findAllRoomBy(
                anyLong(), anyInt(), anyList(), anyList(), isNull(), isNull(), isNull(), isNull()))
                .thenReturn(List.of(
                        Room.builder().id(1L).roomType(roomType1).user(users)
                                .transactions(List.of(RoomTransaction.builder().transactionType(txType1).build()))
                                .build(),
                        Room.builder().id(2L).roomType(roomType1).user(users)
                                .transactions(List.of(RoomTransaction.builder().transactionType(txType1).build()))
                                .build(),
                        Room.builder().id(3L).roomType(roomType1).user(users)
                                .transactions(List.of(RoomTransaction.builder().transactionType(txType1).build()))
                                .build(),
                        Room.builder().id(4L).user(users)
                                .transactions(List.of(RoomTransaction.builder().transactionType(txType1).build()))
                                .build(),
                        Room.builder().id(5L).user(users)
                                .transactions(List.of(RoomTransaction.builder().transactionType(txType1).build()))
                                .build()
                ));

        // When
        List<RoomAbbrResponse> result = roomQueryPageService
                .getRoomsPageBy(1L, 10000L, 10, List.of(), List.of(),
                        null, null, null, null, mode);

        // Then
        assertEquals(3, result.size());
    }


    @Test
    @DisplayName("RoomQueryPageService.getRoomsPageBy() - 방 거래 유형이 매핑되지 않은 결과가 있어도 결과에서만 제외될뿐 예외는 발생하지 않는다.")
    void shouldNotThrowsExceptionWhenPagingRoomWithNoRoomTxMappedResult() {
        // Given
        Users users = Users.builder().id(1L).nickName("Mock User Name").build();
        when(roomRepository.findAllRoomBy(
                anyLong(), anyInt(), anyList(), anyList(), isNull(), isNull(), isNull(), isNull()))
                .thenReturn(List.of(
                        Room.builder().id(1L).roomType(roomType1).user(users)
                                .transactions(List.of(RoomTransaction.builder().transactionType(txType1).build()))
                                .build(),
                        Room.builder().id(2L).roomType(roomType1).user(users)
                                .transactions(List.of(RoomTransaction.builder().transactionType(txType1).build()))
                                .build(),
                        Room.builder().id(3L).roomType(roomType1).user(users)
                                .build(),
                        Room.builder().id(4L).roomType(roomType1).user(users)
                                .build(),
                        Room.builder().id(5L).roomType(roomType1).user(users)
                                .build()
                ));

        // When
        List<RoomAbbrResponse> result = roomQueryPageService
                .getRoomsPageBy(1L, 10000L, 10, List.of(), List.of(),
                        null, null, null, null, mode);

        // Then
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("RoomQueryPageService.getRoomsPageBy() - 방 거래 유형이 너무 많아도 않은 결과가 있어도 결과에서만 제외될뿐 예외는 발생하지 않는다.")
    void shouldNotThrowsExceptionWhenPagingRoomWithExceedRoomTxLimit() {
        // Given
        Users users = Users.builder().id(1L).nickName("Mock User Name").build();
        when(roomRepository.findAllRoomBy(
                anyLong(), anyInt(), anyList(), anyList(), isNull(), isNull(), isNull(), isNull()))
                .thenReturn(List.of(
                        Room.builder().id(1L).roomType(roomType1).user(users)
                                .transactions(List.of(RoomTransaction.builder().transactionType(txType1).build()))
                                .build(),
                        Room.builder().id(2L).roomType(roomType1).user(users)
                                .transactions(List.of(RoomTransaction.builder().transactionType(txType1).build()))
                                .build(),
                        Room.builder().id(3L).roomType(roomType1).user(users)
                                .transactions(List.of(RoomTransaction.builder().transactionType(txType1).build()))
                                .build(),
                        Room.builder().id(4L).roomType(roomType1).user(users)
                                .transactions(List.of(RoomTransaction.builder().transactionType(txType1).build()))
                                .build(),
                        Room.builder().id(5L).roomType(roomType1).user(users)
                                .transactions(List.of(
                                        RoomTransaction.builder().transactionType(txType1).build(),
                                        RoomTransaction.builder().transactionType(txType1).build(),
                                        RoomTransaction.builder().transactionType(txType2).build()
                                ))
                                .build()
                ));

        // When
        List<RoomAbbrResponse> result = roomQueryPageService
                .getRoomsPageBy(1L, 10000L, 10, List.of(), List.of(),
                        null, null, null, null, mode);

        // Then
        assertEquals(4, result.size());
    }
}
