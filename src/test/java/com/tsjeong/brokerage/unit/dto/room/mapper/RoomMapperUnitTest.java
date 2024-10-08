package com.tsjeong.brokerage.unit.dto.room.mapper;

import com.tsjeong.brokerage.dto.room.mapper.RoomMapper;
import com.tsjeong.brokerage.dto.room.response.RoomAbbrResponse;
import com.tsjeong.brokerage.dto.room.response.RoomDetailResponse;
import com.tsjeong.brokerage.entity.room.*;
import com.tsjeong.brokerage.entity.user.Users;
import com.tsjeong.brokerage.exception.ApplicationException;
import com.tsjeong.brokerage.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoomMapperUnitTest {
    private final String userName = "userName";
    private final String roomTypeName = "roomTypeName";
    private final String txTypeName = "txTypeName";
    private final Users users =  Users.builder().id(1L).nickName(userName).build();
    private final RoomType roomType = RoomType.builder().id(1).name(roomTypeName).build();
    private final TransactionType txType = TransactionType.builder().id(1).name(txTypeName).build();
    private final RoomTransaction roomTx1 = RoomTransaction.builder().id(1L).transactionType(txType).build();
    private final RoomTransaction roomTx2 = RoomTransaction.builder().id(2L).transactionType(txType).build();
    private final RoomDetail roomDetail = RoomDetail.builder().description("roomDetailDescription").build();

    @Test
    @DisplayName("RoomMapper.toRoomAbbrResponse() - 본인의 Room 은 editable, deletable 하다")
    void shouldEditAndDeletableUsersOwnRoom() {
        // Given
        Room room = Room.builder()
                .user(users)
                .roomType(roomType)
                .transactions(List.of(roomTx1, roomTx2))
                .detail(roomDetail)
                .build();

        // When
        RoomAbbrResponse abbr = RoomMapper.toRoomAbbrResponse(room, 1L);
        RoomDetailResponse detail = RoomMapper.toRoomDetailResponse(room, 1L);

        // Then
        assertNotNull(abbr);
        assertNotNull(detail);

        assertEquals(userName, abbr.userName());
        assertEquals(userName, detail.userName());

        assertEquals(roomTypeName, abbr.roomTypeName());
        assertEquals(roomTypeName, detail.roomTypeName());

        assertEquals(2, abbr.transactions().size());
        assertEquals(2, detail.transactions().size());

        assertTrue(abbr.isDeletable());
        assertTrue(abbr.isEditable());
        assertTrue(detail.isDeletable());
        assertTrue(detail.isEditable());
    }

    @Test
    @DisplayName("RoomMapper.toRoomAbbrResponse() - 본인의 Room 이 아니면 editable, deletable 하지 않다.")
    void shouldNotEditAndDeletableOtherUsersRoom() {
        // Given
        Room room = Room.builder()
                .user(users)
                .roomType(roomType)
                .transactions(List.of(roomTx1, roomTx2))
                .detail(roomDetail)
                .build();

        // When
        RoomAbbrResponse abbr = RoomMapper.toRoomAbbrResponse(room, 2L);
        RoomDetailResponse detail = RoomMapper.toRoomDetailResponse(room, 2L);

        // Then
        assertNotNull(abbr);
        assertNotNull(detail);

        assertEquals(userName, abbr.userName());
        assertEquals(userName, detail.userName());

        assertEquals(roomTypeName, abbr.roomTypeName());
        assertEquals(roomTypeName, detail.roomTypeName());

        assertEquals(2, abbr.transactions().size());
        assertEquals(2, detail.transactions().size());

        assertFalse(abbr.isDeletable());
        assertFalse(abbr.isEditable());
        assertFalse(detail.isDeletable());
        assertFalse(detail.isEditable());
    }

    @Test
    @DisplayName("RoomMapper.toRoomAbbrResponse() - 등록자 정보가 누락된 경우 예외 발생")
    void shouldThrowExceptionWhenUserIsNull() {
        // Given
        Room room = Room.builder()
                .roomType(roomType)
                .transactions(List.of(roomTx1, roomTx2))
                .detail(roomDetail)
                .build();

        // When, Then
        ApplicationException ape1 = assertThrows(ApplicationException.class, () -> RoomMapper.toRoomAbbrResponse(room, 1L));
        ApplicationException ape2 = assertThrows(ApplicationException.class, () -> RoomMapper.toRoomDetailResponse(room, 1L));

        assertEquals(ErrorCode.MISSING_REQUIRED_RELATION.getCode(), ape1.getCode());
        assertEquals(ErrorCode.MISSING_REQUIRED_RELATION.getCode(), ape2.getCode());
    }

    @Test
    @DisplayName("RoomMapper.toRoomAbbrResponse() - 방 유형 정보가 누락된 경우 예외 발생")
    void shouldThrowExceptionWhenRoomTypeIsNull() {
        // Given
        Room room = Room.builder()
                .user(users)
                .transactions(List.of(roomTx1, roomTx2))
                .detail(roomDetail)
                .build();

        // When, Then
        ApplicationException ape1 = assertThrows(ApplicationException.class, () -> RoomMapper.toRoomAbbrResponse(room, 1L));
        ApplicationException ape2 = assertThrows(ApplicationException.class, () -> RoomMapper.toRoomDetailResponse(room, 1L));

        assertEquals(ErrorCode.MISSING_REQUIRED_RELATION.getCode(), ape1.getCode());
        assertEquals(ErrorCode.MISSING_REQUIRED_RELATION.getCode(), ape2.getCode());
    }

    @Test
    @DisplayName("RoomMapper.toRoomAbbrResponse() - 방 유형 정보가 누락된 경우 예외 발생")
    void shouldThrowExceptionWhenRoomTransactionTypeIsNull() {
        // Given
        Room room = Room.builder()
                .user(users)
                .roomType(roomType)
                .detail(roomDetail)
                .build();

        // When, Then
        ApplicationException ape1 = assertThrows(ApplicationException.class, () -> RoomMapper.toRoomAbbrResponse(room, 1L));
        ApplicationException ape2 = assertThrows(ApplicationException.class, () -> RoomMapper.toRoomDetailResponse(room, 1L));

        assertEquals(ErrorCode.MISSING_REQUIRED_RELATION.getCode(), ape1.getCode());
        assertEquals(ErrorCode.MISSING_REQUIRED_RELATION.getCode(), ape2.getCode());
    }

    @Test
    @DisplayName("RoomMapper.toRoomAbbrResponse() - 방 유형 정보가 너무 많은 경우 예외 발생")
    void shouldThrowExceptionWhenRoomTransactionTypesAreTooMany() {
        // Given
        Room room = Room.builder()
                .user(users)
                .roomType(roomType)
                .transactions(List.of(roomTx1, roomTx2, roomTx1))
                .detail(roomDetail)
                .build();

        // When, Then
        ApplicationException ape1 = assertThrows(ApplicationException.class, () -> RoomMapper.toRoomAbbrResponse(room, 1L));
        ApplicationException ape2 = assertThrows(ApplicationException.class, () -> RoomMapper.toRoomDetailResponse(room, 1L));

        assertEquals(ErrorCode.ENTITY_READ_CONFLICT.getCode(), ape1.getCode());
        assertEquals(ErrorCode.ENTITY_READ_CONFLICT.getCode(), ape2.getCode());
    }
}
