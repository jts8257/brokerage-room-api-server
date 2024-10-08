package com.tsjeong.brokerage.repsoitory.room;

import com.tsjeong.brokerage.entity.room.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionTypeRepository extends JpaRepository<TransactionType, Integer> {
}
