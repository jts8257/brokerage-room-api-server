package com.tsjeong.brokerage.repsoitory.room;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsjeong.brokerage.entity.room.QRoom;
import com.tsjeong.brokerage.entity.room.QRoomTransaction;
import com.tsjeong.brokerage.entity.room.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoomPaginationRepositoryImpl implements RoomPaginationRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<Room> findAllRoomBy(
            long lastRoomId,
            int pageSize,
            List<Integer> roomTypeIds,
            List<Integer> transactionTypeIds,
            BigDecimal minRent,
            BigDecimal maxRent,
            BigDecimal minDeposit,
            BigDecimal maxDeposit
    ) {

        QRoom room = QRoom.room;
        QRoomTransaction roomTransaction = QRoomTransaction.roomTransaction;

        BooleanBuilder builder = getCommonConditions(
                room, roomTransaction, lastRoomId,
                roomTypeIds, transactionTypeIds, minRent, maxRent, minDeposit, maxDeposit
        );

        return queryFactory.selectFrom(room)
                .innerJoin(room.roomType).fetchJoin()
                .innerJoin(room.user).fetchJoin()
                .innerJoin(room.transactions, roomTransaction).fetchJoin()
                .innerJoin(roomTransaction.transactionType).fetchJoin()
                .where(builder)
                .orderBy(room.id.desc())
                .limit(pageSize)
                .fetch();
    }


    @Override
    public List<Room> findAllRoomBy(
            long userId,
            long lastRoomId,
            int pageSize,
            List<Integer> roomTypeIds,
            List<Integer> transactionTypeIds,
            BigDecimal minRent,
            BigDecimal maxRent,
            BigDecimal minDeposit,
            BigDecimal maxDeposit
    ){

        QRoom room = QRoom.room;
        QRoomTransaction roomTransaction = QRoomTransaction.roomTransaction;

        BooleanBuilder builder = getCommonConditions(
                room, roomTransaction, lastRoomId,
                roomTypeIds, transactionTypeIds, minRent, maxRent, minDeposit, maxDeposit
        );

        return queryFactory.selectFrom(room)
                .innerJoin(room.roomType).fetchJoin()
                .innerJoin(room.user).on(room.user.id.eq(userId))
                .innerJoin(room.transactions, roomTransaction).fetchJoin()
                .innerJoin(roomTransaction.transactionType).fetchJoin()
                .where(builder)
                .orderBy(room.id.desc())
                .limit(pageSize)
                .fetch();
    }

    private BooleanBuilder getCommonConditions(
            QRoom room,
            QRoomTransaction roomTransaction,
            long lastRoomId,
            List<Integer> roomTypeIds,
            List<Integer> transactionTypeIds,
            BigDecimal minRent,
            BigDecimal maxRent,
            BigDecimal minDeposit,
            BigDecimal maxDeposit
    ) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(room.id.lt(lastRoomId));

        // RoomType 조건 추가
        if (roomTypeIds != null && !roomTypeIds.isEmpty()) {
            builder.and(room.roomType.id.in(roomTypeIds));
        }

        // TransactionType 조건 추가
        if (transactionTypeIds != null && !transactionTypeIds.isEmpty()) {
            builder.and(roomTransaction.transactionType.id.in(transactionTypeIds));
        }

        // Rent 범위 조건 추가
        if (minRent != null) {
            builder.and(roomTransaction.rentMonthly.goe(minRent));
        }
        if (maxRent != null) {
            builder.and(roomTransaction.rentMonthly.loe(maxRent));
            if (minRent == null) {
                builder.or(roomTransaction.rentMonthly.isNull());
            }
        }

        if (minDeposit != null) {
            builder.and(roomTransaction.deposit.goe(minDeposit));
        }

        if (maxDeposit != null) {
            builder.and(roomTransaction.deposit.loe(maxDeposit));
        }

        return builder;
    }
}
