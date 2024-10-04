package com.tsjeong.brokerage.unit.service.room.command;

import com.tsjeong.brokerage.entity.room.Room;
import com.tsjeong.brokerage.entity.room.RoomType;
import com.tsjeong.brokerage.entity.user.Users;
import com.tsjeong.brokerage.exception.ApplicationException;
import com.tsjeong.brokerage.repsoitory.room.RoomRepository;
import com.tsjeong.brokerage.service.room.command.RoomCreateService;
import com.tsjeong.brokerage.service.room.command.RoomTransactionCreateService;
import com.tsjeong.brokerage.service.room.query.RoomTypeReadService;
import com.tsjeong.brokerage.service.user.UserReadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RoomCreateServiceUnitTest {

    @InjectMocks
    private RoomCreateService roomCreateService;

    @Mock
    private UserReadService userReadService;
    @Mock
    private RoomTypeReadService roomTypeReadService;
    @Mock
    private RoomTransactionCreateService roomTransactionCreateService;
    @Mock
    private RoomRepository roomRepository;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(roomRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // 이 unit test 에서 거래유형 생성 여부는 중요한게 아니다. 아무것도 만들어지지않는걸로 mocking 한다.
        when(roomTransactionCreateService.createRoomTransactionBy(any(), anyList()))
                .thenReturn(List.of());
    }


    @Test
    @DisplayName("createRoom - 성공적으로 Room 저장")
    void shouldReturnValidRoomWhenAllParameterFilled() {
        // Given
        long userId = 1100;
        int roomTypeId = 2;

        Users user = Users.builder().id(userId).nickName("홍길동").build();
        RoomType roomType = RoomType.builder().id(roomTypeId).name("투룸").build();

        when(userReadService.getUsersById(userId)).thenReturn(user);
        when(roomTypeReadService.getRoomTypeById(anyInt())).thenReturn(roomType);

        // When
        Room room = roomCreateService.createRoom(userId,roomTypeId, "jibunAddress", "roadAddress", "address detail", "detail description", List.of());

        // Then
        assertEquals(user, room.getUser());
        assertEquals(roomType, room.getRoomType());
        assertEquals("jibunAddress", room.getAddressJibun());
        assertEquals("roadAddress", room.getAddressRoad());
        assertEquals("address detail", room.getAddressDetail());
        assertEquals("detail description", room.getDetail().getDescription());

        verify(userReadService, times(1)).getUsersById(userId);
        verify(roomTypeReadService, times(1)).getRoomTypeById(roomTypeId);
        verify(roomRepository, times(1)).save(any());
    }


    @Test
    @DisplayName("createRoom - 주소 하나만 있어도 Room 저장")
    void shouldReturnValidRoomWhenOneAddressProvided() {
        // Given
        long userId = 1100;
        int roomTypeId = 2;

        Users user = Users.builder().id(userId).nickName("홍길동").build();
        RoomType roomType = RoomType.builder().id(roomTypeId).name("투룸").build();

        when(userReadService.getUsersById(userId)).thenReturn(user);
        when(roomTypeReadService.getRoomTypeById(anyInt())).thenReturn(roomType);

        // When
        Room room = roomCreateService.createRoom(userId,roomTypeId, "", "roadAddress", "address detail", "detail description", List.of());

        // Then
        assertEquals(user, room.getUser());
        assertEquals(roomType, room.getRoomType());
        assertEquals("", room.getAddressJibun());
        assertEquals("roadAddress", room.getAddressRoad());
        assertEquals("address detail", room.getAddressDetail());
        assertEquals("detail description", room.getDetail().getDescription());

        verify(userReadService, times(1)).getUsersById(userId);
        verify(roomTypeReadService, times(1)).getRoomTypeById(roomTypeId);
        verify(roomRepository, times(1)).save(any());
    }


    @Test
    @DisplayName("createRoom - 지번, 도로명 주소가 모두 없을 경우 예외 발생")
    void shouldThrowExceptionWhenAllAddressIsEmpty() {
        // Given
        long userId = 1100;
        int roomTypeId = 2;

        Users user = Users.builder().id(userId).nickName("홍길동").build();
        RoomType roomType = RoomType.builder().id(roomTypeId).name("투룸").build();

        when(userReadService.getUsersById(userId)).thenReturn(user);
        when(roomTypeReadService.getRoomTypeById(anyInt())).thenReturn(roomType);

        // When & Then
        ApplicationException ape = assertThrows(ApplicationException.class,
                () -> roomCreateService.createRoom(userId,roomTypeId, "", "", "address detail", "detail description", List.of()));


        assertEquals("지번주소 또는 도로명주소 중 하나는 필수입니다.", ape.getMessage());
        verify(userReadService, times(1)).getUsersById(userId);
        verify(roomTypeReadService, times(1)).getRoomTypeById(roomTypeId);
        verify(roomRepository, never()).save(any());
    }

}
