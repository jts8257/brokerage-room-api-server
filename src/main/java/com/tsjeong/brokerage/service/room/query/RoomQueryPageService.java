package com.tsjeong.brokerage.service.room.query;

import com.tsjeong.brokerage.dto.room.response.RoomAbbrResponse;
import com.tsjeong.brokerage.entity.room.Room;
import com.tsjeong.brokerage.entity.room.RoomType;
import com.tsjeong.brokerage.entity.room.TransactionType;
import com.tsjeong.brokerage.service.category.RoomTypeReadService;
import com.tsjeong.brokerage.service.category.TransactionTypeReadService;
import com.tsjeong.brokerage.service.room.query.enums.RoomQueryMode;
import com.tsjeong.brokerage.service.room.query.strategy.RoomPaginationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomQueryPageService {

    private final RoomPaginationStrategy allRoomPaginationStrategy;
    private final RoomPaginationStrategy myRoomPaginationStrategy;
    private final RoomTypeReadService roomTypeReadService;
    private final TransactionTypeReadService transactionTypeReadService;

    @Transactional(readOnly = true)
    public List<RoomAbbrResponse> getRoomsPageBy(
            long actionUserId,
            long lastRoomId,
            int pageSize,
            List<Integer> roomTypeIds,
            List<Integer> transactionTypeIds,
            BigDecimal minRent,
            BigDecimal maxRent,
            BigDecimal minDeposit,
            BigDecimal maxDeposit,
            RoomQueryMode mode
    ) {

        RoomPaginationStrategy strategy = switch (mode) {
            case ALL -> allRoomPaginationStrategy;
            case MY -> myRoomPaginationStrategy;
        };

        if (roomTypeIds == null || roomTypeIds.isEmpty()) {
            roomTypeIds = roomTypeReadService.getAllRoomTypes().stream()
                    .map(RoomType::getId)
                    .toList();
        }

        if (transactionTypeIds == null || transactionTypeIds.isEmpty()) {
            transactionTypeIds = transactionTypeReadService.getAllTransactionTypes().stream()
                    .map(TransactionType::getId)
                    .toList();
        }

        List<Room> rooms = strategy.execute(
                actionUserId,
                lastRoomId,
                pageSize,
                roomTypeIds,
                transactionTypeIds,
                minRent,
                maxRent,
                minDeposit,
                maxDeposit
        );

        var responses = rooms.stream().map(RoomAbbrResponse::new).toList();
        System.out.println("responses: " + responses);
        return rooms.stream().map(RoomAbbrResponse::new).toList();
    }
}
