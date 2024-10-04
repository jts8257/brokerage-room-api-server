package com.tsjeong.brokerage.service.room.query;

import com.tsjeong.brokerage.dto.room.response.RoomAbbrResponse;
import com.tsjeong.brokerage.service.room.query.enums.RoomQueryMode;
import com.tsjeong.brokerage.service.room.query.enums.RoomQueryRoomType;
import com.tsjeong.brokerage.service.room.query.enums.RoomQueryTransactionType;
import com.tsjeong.brokerage.service.room.query.strategy.RoomPaginationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomQueryPageService {

    private final RoomPaginationStrategy allRoomPaginationStrategy;
    private final RoomPaginationStrategy myRoomPaginationStrategy;

    @Transactional(readOnly = true)
    public List<RoomAbbrResponse> getRoomsPageBy(
            long actionUserId,
            long lastRoomId,
            int pageSize,
            List<RoomQueryRoomType> roomTypes,
            List<RoomQueryTransactionType> transactionTypes,
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

        List<Integer> roomTypeIds = roomTypes == null ? null : roomTypes.stream().map(RoomQueryRoomType::getId).toList();

        List<Integer> transactionTypeIds = transactionTypes == null ? null :
                transactionTypes.stream().map(RoomQueryTransactionType::getId).toList();

        return strategy.execute(
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
    }
}
