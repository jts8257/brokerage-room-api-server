package com.tsjeong.brokerage.repsoitory.room;

import com.tsjeong.brokerage.entity.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
