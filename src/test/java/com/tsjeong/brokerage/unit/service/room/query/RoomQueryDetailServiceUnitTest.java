package com.tsjeong.brokerage.unit.service.room.query;

import com.tsjeong.brokerage.exception.ApplicationException;
import com.tsjeong.brokerage.exception.ErrorCode;
import com.tsjeong.brokerage.repsoitory.room.RoomRepository;
import com.tsjeong.brokerage.service.room.query.RoomQueryDetailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class RoomQueryDetailServiceUnitTest {

    @InjectMocks
    private RoomQueryDetailService roomQueryDetailService;

    @Mock
    private RoomRepository roomRepository;


    @Test
    @DisplayName("RoomQueryDetailService.getRoomForDetailQuery() - Room 을 못찾으면 예외 발생")
    void shouldThrowExceptionWhenNoRoomFound() {
        // Given
        MockitoAnnotations.openMocks(this);
        when(roomRepository.findRoomByIdFetchDetail(anyLong())).thenReturn(Optional.empty());

        // When, Then
        ApplicationException ape = assertThrows(
                ApplicationException.class,
                () -> roomQueryDetailService.getRoomForDetailQuery(1L,1L)
        );

        assertEquals(ErrorCode.ENTITY_NOT_FOUND.getCode(), ape.getCode());
    }
}
