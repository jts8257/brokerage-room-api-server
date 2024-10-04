package com.tsjeong.brokerage.service.room.query.strategy;

import com.tsjeong.brokerage.dto.room.response.RoomAbbrResponse;
import com.tsjeong.brokerage.repsoitory.room.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AllRoomPaginationStrategy implements RoomPaginationStrategy {

    private final RoomRepository roomRepository;

    @Override
    public List<RoomAbbrResponse> execute(
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

         return roomRepository.findAllRoomBy(lastRoomId, pageSize,
                        roomTypeIds, transactionTypeIds, minRent, maxRent, minDeposit, maxDeposit).stream()
                .map(RoomAbbrResponse::new)
                .toList();

    }
}
