package com.tsjeong.brokerage.service.category;

import com.tsjeong.brokerage.entity.room.RoomType;

import java.util.List;

public interface RoomTypeReadService {
    /**
     *
     * @param roomTypeId 조회하려는 RoomType 의 id
     * @return RoomType
     * @throws com.tsjeong.brokerage.exception.ApplicationException 해당 id 의 roomType 이 존재하지 않을 떄
     * @throws IllegalArgumentException roomTypeId 가 null 일때
     */
    RoomType getRoomTypeById(Integer roomTypeId);


    List<RoomType> getAllRoomTypes();
}
