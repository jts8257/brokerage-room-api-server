package com.tsjeong.brokerage.service.room.query.strategy;

import com.tsjeong.brokerage.entity.room.Room;
import com.tsjeong.brokerage.repsoitory.room.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MyRoomPaginationStrategy implements RoomPaginationStrategy {

    private final RoomRepository roomRepository;

    @Override
    public List<Room> execute(
            long actionUserId,
            long lastRoomId,
            int pageSize,
            List<Integer> roomTypeIds,
            List<Integer> transactionTypeIds,
            BigDecimal minRent,
            BigDecimal maxRent,
            BigDecimal minDeposit,
            BigDecimal maxDeposit
    ) {

        return roomRepository.findAllRoomBy(actionUserId, lastRoomId, pageSize,
                roomTypeIds, transactionTypeIds, minRent, maxRent, minDeposit, maxDeposit);
    }
}
