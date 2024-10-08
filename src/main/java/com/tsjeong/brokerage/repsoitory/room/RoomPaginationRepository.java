package com.tsjeong.brokerage.repsoitory.room;

import com.tsjeong.brokerage.entity.room.Room;

import java.math.BigDecimal;
import java.util.List;

public interface RoomPaginationRepository {

    List<Room> findAllRoomBy(
            long lastRoomId,
            int pageSize,
            List<Integer> roomTypeIds,
            List<Integer> transactionTypeIds,
            BigDecimal minRent,
            BigDecimal maxRent,
            BigDecimal minDeposit,
            BigDecimal maxDeposit
    );

    List<Room> findAllRoomBy(
            long userId,
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
