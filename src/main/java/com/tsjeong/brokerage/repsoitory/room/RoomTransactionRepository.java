package com.tsjeong.brokerage.repsoitory.room;

import com.tsjeong.brokerage.entity.room.RoomTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomTransactionRepository extends JpaRepository<RoomTransaction, Long> {
}
