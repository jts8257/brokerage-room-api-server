package com.tsjeong.brokerage.repsoitory.room;

import com.tsjeong.brokerage.entity.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {


    @Query("""
    SELECT r FROM Room r
    INNER JOIN FETCH r.user
    INNER JOIN FETCH r.roomType
    LEFT JOIN FETCH r.detail
    WHERE r.id = :roomId
    """)
    Optional<Room> findRoomByIdFetchDetail(Long roomId);
}
