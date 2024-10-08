package com.tsjeong.brokerage.integration.service.room.query;

import com.tsjeong.brokerage.dto.room.response.RoomAbbrResponse;
import com.tsjeong.brokerage.integration.testconfig.RoomTestBase;
import com.tsjeong.brokerage.service.room.query.RoomQueryPageService;
import com.tsjeong.brokerage.service.room.query.enums.RoomQueryMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RoomAllPageServiceIntegrationTest extends RoomTestBase {

    @Autowired
    private RoomQueryPageService roomQueryPageService;

    private final RoomQueryMode mode = RoomQueryMode.ALL;
    private final BigDecimal depositSmall = new BigDecimal("1000");
    private final BigDecimal depositLarge = new BigDecimal("10000");
    private final BigDecimal rentSmall = new BigDecimal("50");
    private final BigDecimal rentLarge = new BigDecimal("100");


    @BeforeEach
    void setup() {
        List<RoomSetupConfig> roomConfigs = List.of(
                // user1, oneRoom, 2개 전세
                new RoomSetupConfig(user1, oneRoom, List.of(new RoomTransactionSetupConfig(jeonse, depositLarge, null))),
                new RoomSetupConfig(user1, oneRoom, List.of(new RoomTransactionSetupConfig(jeonse, depositSmall, null))),
                // user1, oneRoom, 2개 월세
                new RoomSetupConfig(user1, oneRoom, List.of(new RoomTransactionSetupConfig(rent, depositLarge, rentLarge))),
                new RoomSetupConfig(user1, oneRoom, List.of(new RoomTransactionSetupConfig(rent, depositLarge, rentSmall))),
                // user1, twoRoom, 2개 전세
                new RoomSetupConfig(user1, twoRoom, List.of(new RoomTransactionSetupConfig(jeonse, depositLarge, null))),
                new RoomSetupConfig(user1, twoRoom, List.of(new RoomTransactionSetupConfig(jeonse, depositSmall, null))),
                // user1, twoRoom, 2개 월세
                new RoomSetupConfig(user1, twoRoom, List.of(new RoomTransactionSetupConfig(rent, depositLarge, rentLarge))),
                new RoomSetupConfig(user1, twoRoom, List.of(new RoomTransactionSetupConfig(rent, depositLarge, rentSmall))),
                // user1, oneRoom, 2개 전세
                new RoomSetupConfig(user2, oneRoom, List.of(new RoomTransactionSetupConfig(jeonse, depositLarge, null))),
                new RoomSetupConfig(user2, oneRoom, List.of(new RoomTransactionSetupConfig(jeonse, depositSmall, null))),
                // user1, oneRoom, 2개 월세
                new RoomSetupConfig(user2, oneRoom, List.of(new RoomTransactionSetupConfig(rent, depositLarge, rentLarge))),
                new RoomSetupConfig(user2, oneRoom, List.of(new RoomTransactionSetupConfig(rent, depositLarge, rentSmall))),
                // user1, twoRoom, 2개 전세
                new RoomSetupConfig(user2, twoRoom, List.of(new RoomTransactionSetupConfig(jeonse, depositLarge, null))),
                new RoomSetupConfig(user2, twoRoom, List.of(new RoomTransactionSetupConfig(jeonse, depositSmall, null))),
                // user1, twoRoom, 2개 월세
                new RoomSetupConfig(user2, twoRoom, List.of(new RoomTransactionSetupConfig(rent, depositLarge, rentLarge))),
                new RoomSetupConfig(user2, twoRoom, List.of(new RoomTransactionSetupConfig(rent, depositLarge, rentSmall)))
        );

        createRoomsWithTransactions(roomConfigs);
    }

    @Test
    @DisplayName("RoomQueryPageService.getRoomsPageBy(ALL) - 어떤 제약 조건도 없을때는 pageSize 만큼 페이지를 반환한다.")
    void shouldReturnSizePagesWhenNoFilterIndicated() {
        // Given
        long lastRoomId = Long.MAX_VALUE;
        int pageSize = 10;
        List<Integer> roomTypeIds = null;
        List<Integer> transactionTypeIds = null;
        BigDecimal minRent = null;
        BigDecimal maxRent = null;
        BigDecimal minDeposit = null;
        BigDecimal maxDeposit = null;

        // When
        List<RoomAbbrResponse> rooms = roomQueryPageService.getRoomsPageBy(
                user1Id, lastRoomId, pageSize, roomTypeIds, transactionTypeIds, minRent, maxRent, minDeposit, maxDeposit, mode
        );

        // Then
        assertNotNull(rooms);
        assertEquals(pageSize, rooms.size(), "조회된 방의 개수가 pageSize 제약 조건을 충족하지 못합니다.");
    }

    @Test
    @DisplayName("RoomQueryPageService.getRoomsPageBy(ALL) - 룸타입에 따른 조회 성공")
    void shouldReturnRooPagesWhenRoomTypeFiltered() {
        // Given
        long lastRoomId = Long.MAX_VALUE;
        int pageSize = 50;
        List<Integer> roomTypeIds1 = List.of(oneRoomId);
        List<Integer> roomTypeIds2 = List.of(oneRoomId, twoRoomId);
        List<Integer> transactionTypeIds = null;
        BigDecimal minRent = null;
        BigDecimal maxRent = null;
        BigDecimal minDeposit = null;
        BigDecimal maxDeposit = null;

        String roomName1 = oneRoom.getName();
        String roomName2 = twoRoom.getName();

        // When
        List<RoomAbbrResponse> rooms1 = roomQueryPageService.getRoomsPageBy(
                user1Id, lastRoomId, pageSize, roomTypeIds1, transactionTypeIds, minRent, maxRent, minDeposit, maxDeposit, mode
        );

        List<RoomAbbrResponse> rooms2 = roomQueryPageService.getRoomsPageBy(
                user1Id, lastRoomId, pageSize, roomTypeIds2, transactionTypeIds, minRent, maxRent, minDeposit, maxDeposit, mode
        );

        // Then
        assertNotNull(rooms1);
        assertEquals(8, rooms1.size());
        rooms1.forEach(room -> assertEquals(roomName1, room.roomTypeName(),"%s 을 조회했지만 다른 방이 조회되었습니다.".formatted(roomName1)));

        Set<String> roomNames = Set.of(roomName1, roomName2);
        assertNotNull(rooms2);
        assertEquals(16, rooms2.size());
        rooms2.forEach(room -> assertTrue(roomNames.contains(room.roomTypeName()),"%s 을 조회했지만 다른 방이 조회되었습니다.".formatted(roomNames)));
    }


    @Test
    @DisplayName("RoomQueryPageService.getRoomsPageBy(ALL) - 거래유형에 따른 조회 성공")
    void shouldReturnRoomPagesWhenTransactionTypeFiltered() {
        // Given
        long lastRoomId = Long.MAX_VALUE;
        int pageSize = 50;
        List<Integer> roomTypeIds = null;
        List<Integer> transactionTypeIds1 = List.of(jeonseId);
        List<Integer> transactionTypeIds2 = List.of(jeonseId, rentId);
        BigDecimal minRent = null;
        BigDecimal maxRent = null;
        BigDecimal minDeposit = null;
        BigDecimal maxDeposit = null;

        // When
        List<RoomAbbrResponse> rooms1 = roomQueryPageService.getRoomsPageBy(
                user1Id, lastRoomId, pageSize, roomTypeIds, transactionTypeIds1, minRent, maxRent, minDeposit, maxDeposit, mode
        );

        List<RoomAbbrResponse> rooms2 = roomQueryPageService.getRoomsPageBy(
                user1Id, lastRoomId, pageSize, roomTypeIds, transactionTypeIds2, minRent, maxRent, minDeposit, maxDeposit, mode
        );

        // Then
        Set<String> txNameExpected = Set.of(jeonse.getName());
        Set<String> txNameFound = new HashSet<>();
        assertNotNull(rooms1);
        assertEquals(8, rooms1.size());
        rooms1.forEach(room -> room.transactions().forEach(
                tx ->txNameFound.add(tx.transactionTypeName())
        ));
        assertTrue(txNameExpected.containsAll(txNameFound));

        txNameExpected = Set.of(jeonse.getName(), rent.getName());
        assertNotNull(rooms2);
        assertEquals(16, rooms2.size());
        rooms2.forEach(room -> room.transactions().forEach(
                tx ->txNameFound.add(tx.transactionTypeName())
        ));
        assertTrue(txNameExpected.containsAll(txNameFound));
    }

    @Test
    @DisplayName("RoomQueryPageService.getRoomsPageBy(ALL) - 최저 월세 조건에 따른 조회 결과")
    void shouldReturnRentRoomPagesWhenRentMinFiltered() {
        // Given
        long lastRoomId = Long.MAX_VALUE;
        int pageSize = 50;
        List<Integer> roomTypeIds = null;
        List<Integer> transactionTypeIds = null;
        BigDecimal minRent1 = rentSmall;
        BigDecimal minRent2 = rentLarge;
        BigDecimal maxRent = null;
        BigDecimal minDeposit = null;
        BigDecimal maxDeposit = null;

        // When
        List<RoomAbbrResponse> rooms1 = roomQueryPageService.getRoomsPageBy(
                user1Id, lastRoomId, pageSize, roomTypeIds, transactionTypeIds, minRent1, maxRent, minDeposit, maxDeposit, mode
        );

        List<RoomAbbrResponse> rooms2 = roomQueryPageService.getRoomsPageBy(
                user1Id, lastRoomId, pageSize, roomTypeIds, transactionTypeIds, minRent2, maxRent, minDeposit, maxDeposit, mode
        );

        // Then
        Set<String> txNameExpected = Set.of(rent.getName());
        Set<String> txNameFound = new HashSet<>();

        assertNotNull(rooms1);
        assertEquals(8, rooms1.size());
        rooms1.forEach(room -> room.transactions().forEach(
                tx ->txNameFound.add(tx.transactionTypeName())
        ));
        assertTrue(txNameExpected.containsAll(txNameFound));

        txNameFound.clear();

        assertNotNull(rooms2);
        assertEquals(4, rooms2.size());
        rooms2.forEach(room -> room.transactions().forEach(
                tx ->txNameFound.add(tx.transactionTypeName())
        ));
        assertTrue(txNameExpected.containsAll(txNameFound));
    }


    @Test
    @DisplayName("RoomQueryPageService.getRoomsPageBy(ALL) - 방 유형 조건 없는 최대 월세 조건은 전세도 반환")
    void shouldReturnRoomPagesWhenRentMaxFiltered() {
        // Given
        long lastRoomId = Long.MAX_VALUE;
        int pageSize = 50;
        List<Integer> roomTypeIds = null;
        List<Integer> transactionTypeIds = null;
        BigDecimal minRent = null;
        BigDecimal maxRent1 = rentSmall;
        BigDecimal maxRent2 = rentLarge;
        BigDecimal minDeposit = null;
        BigDecimal maxDeposit = null;

        // When
        List<RoomAbbrResponse> rooms1 = roomQueryPageService.getRoomsPageBy(
                user1Id, lastRoomId, pageSize, roomTypeIds, transactionTypeIds, minRent, maxRent1, minDeposit, maxDeposit, mode
        );

        List<RoomAbbrResponse> rooms2 = roomQueryPageService.getRoomsPageBy(
                user1Id, lastRoomId, pageSize, roomTypeIds, transactionTypeIds, minRent, maxRent2, minDeposit, maxDeposit, mode
        );

        // Then
        Set<String> txFound = new HashSet<>();
        assertNotNull(rooms1);

        assertEquals(12, rooms1.size());
        rooms1.forEach(room -> room.transactions().forEach(
                tx ->txFound.add(tx.transactionTypeName())
        ));
        assertEquals(2, txFound.size());

        txFound.clear();

        assertNotNull(rooms2);
        assertEquals(16, rooms2.size());
        rooms2.forEach(room -> room.transactions().forEach(
                tx ->txFound.add(tx.transactionTypeName())
        ));
        assertEquals(2, txFound.size());
    }

    @Test
    @DisplayName("RoomQueryPageService.getRoomsPageBy(ALL) - 방 하나가 2개 이상의 거래 유형을 가지고 있더라도 카테시안 곱을 반환하지 않는다.")
    void shouldReturnNotCartesianProductAsTxRowMany() {
        // Given
        roomRepository.deleteAll();
        List<RoomSetupConfig> roomConfigs = List.of(
                // user1, oneRoom, 2개 전세
                new RoomSetupConfig(user1, oneRoom, List.of(
                        new RoomTransactionSetupConfig(jeonse, depositLarge, null),
                        new RoomTransactionSetupConfig(rent, depositLarge, rentLarge)
                )),
                new RoomSetupConfig(user1, twoRoom, List.of(
                        new RoomTransactionSetupConfig(jeonse, depositLarge, null),
                        new RoomTransactionSetupConfig(rent, depositLarge, rentLarge)
                )),
                new RoomSetupConfig(user2, oneRoom, List.of(
                        new RoomTransactionSetupConfig(jeonse, depositLarge, null),
                        new RoomTransactionSetupConfig(rent, depositLarge, rentLarge)
                )),
                new RoomSetupConfig(user2, twoRoom, List.of(
                        new RoomTransactionSetupConfig(jeonse, depositLarge, null),
                        new RoomTransactionSetupConfig(rent, depositLarge, rentLarge)
                ))
        );

        createRoomsWithTransactions(roomConfigs);

        long lastRoomId = Long.MAX_VALUE;
        int pageSize = 50;
        List<Integer> roomTypeIds = null;
        List<Integer> transactionTypeIds = null;
        BigDecimal minRent = null;
        BigDecimal maxRent = null;
        BigDecimal minDeposit = null;
        BigDecimal maxDeposit = null;

        // When
        List<RoomAbbrResponse> rooms = roomQueryPageService.getRoomsPageBy(
                user1Id, lastRoomId, pageSize, roomTypeIds, transactionTypeIds, minRent, maxRent, minDeposit, maxDeposit, mode
        );

        // Then
        assertNotNull(rooms);
        assertEquals(4, rooms.size());
    }
}
