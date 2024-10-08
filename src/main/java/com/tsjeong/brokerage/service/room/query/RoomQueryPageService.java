package com.tsjeong.brokerage.service.room.query;

import com.tsjeong.brokerage.dto.room.mapper.RoomMapper;
import com.tsjeong.brokerage.dto.room.response.RoomAbbrResponse;
import com.tsjeong.brokerage.entity.room.Room;
import com.tsjeong.brokerage.entity.room.RoomType;
import com.tsjeong.brokerage.entity.room.TransactionType;
import com.tsjeong.brokerage.exception.ApplicationException;
import com.tsjeong.brokerage.repsoitory.room.RoomPaginationRepository;
import com.tsjeong.brokerage.service.category.RoomTypeReadService;
import com.tsjeong.brokerage.service.category.TransactionTypeReadService;
import com.tsjeong.brokerage.service.room.query.enums.RoomQueryMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class RoomQueryPageService {

    private final RoomPaginationRepository roomPaginationRepository;
    private final RoomTypeReadService roomTypeReadService;
    private final TransactionTypeReadService transactionTypeReadService;

    public RoomQueryPageService(
            RoomPaginationRepository roomPaginationRepository,
            RoomTypeReadService roomTypeReadService,
            TransactionTypeReadService transactionTypeReadService
    ) {
        this.roomPaginationRepository = roomPaginationRepository;
        this.roomTypeReadService = roomTypeReadService;
        this.transactionTypeReadService = transactionTypeReadService;
    }

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

        AtomicReference<BigDecimal> minRef = new AtomicReference<>(minRent);
        AtomicReference<BigDecimal> maxRef = new AtomicReference<>(maxRent);
        minMaxValueSwap(minRef, maxRef);
        minRent = minRef.get();
        maxRent = maxRef.get();

        minRef = new AtomicReference<>(minDeposit);
        maxRef = new AtomicReference<>(maxDeposit);
        minMaxValueSwap(minRef, maxRef);
        minDeposit = minRef.get();
        maxDeposit = maxRef.get();

        List<Room> rooms = switch (mode) {
            case ALL -> roomPaginationRepository.findAllRoomBy(
                    lastRoomId, pageSize, roomTypeIds, transactionTypeIds, minRent, maxRent, minDeposit, maxDeposit);

            case MY -> roomPaginationRepository.findAllRoomBy(
                    actionUserId, lastRoomId, pageSize, roomTypeIds, transactionTypeIds, minRent, maxRent, minDeposit, maxDeposit);
        };

        return rooms.stream()
                .map(room -> {
                    try {
                        return RoomMapper.toRoomAbbrResponse(room, actionUserId);
                    } catch (ApplicationException ape) {
                        log.warn("Failed Mapping for room id {}: ", room.getId(), ape);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    private void minMaxValueSwap(AtomicReference<BigDecimal> minRef, AtomicReference<BigDecimal> maxRef) {
        BigDecimal min = minRef.get();
        BigDecimal max = maxRef.get();
        if (min != null && max != null && min.compareTo(max) > 0) {
            minRef.set(max);
            maxRef.set(min);
        }
    }
}
