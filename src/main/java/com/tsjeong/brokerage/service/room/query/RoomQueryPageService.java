package com.tsjeong.brokerage.service.room.query;

import com.tsjeong.brokerage.dto.room.mapper.RoomMapper;
import com.tsjeong.brokerage.dto.room.response.RoomAbbrResponse;
import com.tsjeong.brokerage.entity.room.Room;
import com.tsjeong.brokerage.entity.room.RoomType;
import com.tsjeong.brokerage.entity.room.TransactionType;
import com.tsjeong.brokerage.repsoitory.room.RoomRepository;
import com.tsjeong.brokerage.service.category.RoomTypeReadService;
import com.tsjeong.brokerage.service.category.TransactionTypeReadService;
import com.tsjeong.brokerage.service.room.query.enums.RoomQueryMode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomQueryPageService {

    private final RoomRepository roomRepository;
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

        List<Room> rooms = switch (mode) {
            case ALL -> roomRepository.findAllRoomBy(lastRoomId, pageSize,
                    roomTypeIds, transactionTypeIds, minRent, maxRent, minDeposit, maxDeposit);

            case MY ->  roomRepository.findAllRoomBy(actionUserId, lastRoomId, pageSize,
                    roomTypeIds, transactionTypeIds, minRent, maxRent, minDeposit, maxDeposit);
        };

        return rooms.stream().map(room -> RoomMapper.toRoomAbbrResponse(room, actionUserId)).toList();
    }
}
