package com.tsjeong.brokerage.unit.service.room.command;

import com.tsjeong.brokerage.dto.room.request.RoomTransactionUpdateRequest;
import com.tsjeong.brokerage.entity.room.Room;
import com.tsjeong.brokerage.entity.room.RoomTransaction;
import com.tsjeong.brokerage.entity.room.RoomType;
import com.tsjeong.brokerage.entity.user.Users;
import com.tsjeong.brokerage.exception.ApplicationException;
import com.tsjeong.brokerage.exception.ErrorCode;
import com.tsjeong.brokerage.repsoitory.room.RoomRepository;
import com.tsjeong.brokerage.service.category.RoomTypeReadService;
import com.tsjeong.brokerage.service.room.command.RoomTransactionUpdateService;
import com.tsjeong.brokerage.service.room.command.RoomUpdateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class RoomUpdateServiceUnitTest {

    @InjectMocks
    private RoomUpdateService roomUpdateService;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private RoomTypeReadService roomTypeReadService;
    @Mock
    private RoomTransactionUpdateService roomTransactionUpdateService;

    private final long useId = 11L;
    private final int roomTypeId = 12;
    private final int notFoundRoomTypeId = 13;
    private final long roomId = 123L;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Users user = Users.builder()
                .id(useId)
                .nickName("Mock User Name")
                .build();

        RoomType roomType = RoomType.builder()
                .id(roomTypeId)
                .name("Mock RoomType")
                .build();

        Room room = Room.builder()
                .user(user)
                .roomType(roomType)
                .build();

        when(roomTypeReadService.getRoomTypeById(roomTypeId)).thenReturn(roomType);
        when(roomTypeReadService.getRoomTypeById(notFoundRoomTypeId))
                .thenThrow(ErrorCode.ENTITY_NOT_FOUND.build());

        when(roomTransactionUpdateService.updateRoomTransactionBy(any(), anyList()))
                .thenReturn(new ArrayList<>());

        when(roomRepository.findRoomForUpdateUsingId(roomId)).thenReturn(Optional.of(room));

        when(roomRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    @DisplayName("RoomUpdateService.updateRoom() - Mock 설정대로 하면 에외 발생 없음")
    void shouldNotThrowExceptionWhenFollowMockSetting() {
        assertDoesNotThrow(() -> roomUpdateService.roomUpdate(useId, roomId, roomTypeId,
                "jibunAddress", "roadAddress", "", "",
                List.of(new RoomTransactionUpdateRequest(), new RoomTransactionUpdateRequest())
        ));
    }

    @Test
    @DisplayName("RoomUpdateService.updateRoom() - Room 을 못찾으면 예외 발생")
    void shouldThrowExceptionWhenNoRoomFound() {

        ApplicationException ape = assertThrows(
                ApplicationException.class,
                () -> roomUpdateService.roomUpdate(useId, roomId + 1, roomTypeId,
                "jibunAddress", "roadAddress", "", "", List.of())
        );

        assertEquals(ErrorCode.ENTITY_NOT_FOUND.getCode(), ape.getCode());
    }

    @Test
    @DisplayName("RoomUpdateService.updateRoom() - 다른 유저의 Room 을 수정하려고 하면 예외 발생")
    void shouldThrowExceptionWhenTryingToUpdateOtherUsersRoom() {

        ApplicationException ape = assertThrows(
                ApplicationException.class,
                () -> roomUpdateService.roomUpdate(useId + 1, roomId, roomTypeId,
                        "jibunAddress", "roadAddress", "", "", List.of())
        );

        assertEquals(ErrorCode.USER_NOT_ACCESSIBLE.getCode(), ape.getCode());
    }

    @Test
    @DisplayName("RoomUpdateService.updateRoom() - Room 을 찾지 못하면 예외 발생")
    void shouldThrowExceptionWhenRoomTypeNotFound() {

        ApplicationException ape = assertThrows(
                ApplicationException.class,
                () -> roomUpdateService.roomUpdate(useId, roomId, notFoundRoomTypeId,
                        "jibunAddress", "roadAddress", "", "", List.of())
        );

        assertEquals(ErrorCode.ENTITY_NOT_FOUND.getCode(), ape.getCode());
    }

    @Test
    @DisplayName("RoomUpdateService.updateRoom() - 거래유형이 비어있으면 예외 발생")
    void shouldThrowExceptionWhenEmptyRoomTxRequest() {

        ApplicationException apeEmpty = assertThrows(
                ApplicationException.class,
                () -> roomUpdateService.roomUpdate(useId, roomId, roomTypeId,
                "jibunAddress", "roadAddress", "", "", List.of()
        ));

        ApplicationException apeNull = assertThrows(
                ApplicationException.class,
                () -> roomUpdateService.roomUpdate(useId, roomId, roomTypeId,
                "jibunAddress", "roadAddress", "", "", null
        ));

        assertEquals(ErrorCode.ENTITY_REGISTER_CONFLICT.getCode(), apeEmpty.getCode());
        assertEquals(ErrorCode.ENTITY_REGISTER_CONFLICT.getCode(), apeNull.getCode());
    }

    @Test
    @DisplayName("RoomUpdateService.updateRoom() - 지번, 도로명 주소가 둘다 누락되었으면 예외 발생")
    void shouldThrowExceptionWhenBothAddressAreNull() {
        ApplicationException ape = assertThrows(
                ApplicationException.class,
                () -> roomUpdateService.roomUpdate(useId, roomId, roomTypeId,
                        "", "", "", "", List.of()
                ));

        assertEquals(ErrorCode.BAD_CONSTRAINT_BODY_FIELD.getCode(), ape.getCode());
    }
    @Test
    @DisplayName("RoomUpdateService.updateRoom() - 거래 유형이 너무 많으면 예외 발생")
    void shouldThrowExceptionWhenExceedRoomTxMaxLimit() {
        // Given
        List<RoomTransactionUpdateRequest> roomTxUpdateRequestList =
                IntStream.range(0, RoomTransaction.MAX_PER_ROOM +1)
                        .mapToObj(i -> new RoomTransactionUpdateRequest())
                        .toList();

        // When, Then
        ApplicationException ape = assertThrows(
                ApplicationException.class,
                () -> roomUpdateService.roomUpdate(useId, roomId, roomTypeId,
                        "jibunAddress", "roadAddress", "", "", roomTxUpdateRequestList
                ));

        assertEquals(ErrorCode.ENTITY_REGISTER_CONFLICT.getCode(), ape.getCode());
    }
}
