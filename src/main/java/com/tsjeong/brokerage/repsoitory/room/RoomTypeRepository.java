package com.tsjeong.brokerage.repsoitory.room;

import com.tsjeong.brokerage.entity.room.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {
}
