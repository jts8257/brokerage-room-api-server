package com.tsjeong.brokerage.service.room.query.strategy;

import com.tsjeong.brokerage.dto.room.response.RoomAbbrResponse;

import java.math.BigDecimal;
import java.util.List;

public interface RoomPaginationStrategy {

    List<RoomAbbrResponse> execute(
            long actionUserId,
            long lastRoomId,
            int pageSize,
            List<Integer> roomTypeIds,
            List<Integer> transactionTypeIds,
            BigDecimal minRent,
            BigDecimal maxRent,
            BigDecimal minDeposit,
            BigDecimal maxDeposit
    );
}
