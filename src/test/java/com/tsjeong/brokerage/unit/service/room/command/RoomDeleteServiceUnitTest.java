package com.tsjeong.brokerage.unit.service.room.command;

import com.tsjeong.brokerage.entity.room.Room;
import com.tsjeong.brokerage.entity.user.Users;
import com.tsjeong.brokerage.exception.ApplicationException;
import com.tsjeong.brokerage.exception.ErrorCode;
import com.tsjeong.brokerage.repsoitory.room.RoomRepository;
import com.tsjeong.brokerage.service.room.command.RoomDeleteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

public class RoomDeleteServiceUnitTest {
    @InjectMocks
    private RoomDeleteService roomDeleteService;

    @Mock
    private RoomRepository roomRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("RoomDeleteService.deleteRoom() - Room 을 찾지 못하면 예외 발생")
    void shouldThrowExceptionWhenNoRoomFound() {
        // Given
        when(roomRepository.findRoomByIdFetchUser(any())).thenReturn(Optional.empty());

        // When, Then
        ApplicationException ape = assertThrows(
                ApplicationException.class,
                () -> roomDeleteService.deleteRoom(1L, 1L)
        );
        assertEquals(ErrorCode.ENTITY_NOT_FOUND.getCode(), ape.getCode());
    }

    @Test
    @DisplayName("RoomDeleteService.deleteRoom() - 다른 사람의 Room 을 지우려고 하면 예외 발생")
    void shouldThrowExceptionWhenTryingToDeleteOthersRoom() {
        // Given
        when(roomRepository.findRoomByIdFetchUser(any()))
                .thenReturn(Optional.of(
                        Room.builder()
                                .user(Users.builder().id(10L).build())
                                .build()
                ));

        // When, Then
        ApplicationException ape = assertThrows(
                ApplicationException.class,
                () -> roomDeleteService.deleteRoom(1L, 1L)
        );
        assertEquals(ErrorCode.USER_NOT_ACCESSIBLE.getCode(), ape.getCode());
    }
}
