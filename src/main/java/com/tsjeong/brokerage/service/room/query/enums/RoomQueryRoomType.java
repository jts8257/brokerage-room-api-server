package com.tsjeong.brokerage.service.room.query.enums;

import lombok.Getter;

@Getter
public enum RoomQueryRoomType {
    // DB 와의 관계에서는 확장성을 위해 ENUM 을 Entity 에 넣지 않았지만 클라이언트와의 관계에서는 ENUM 을 통해 직관적으로 요청을 처리한다.
    ONE_ROOM(1), TWO_ROOM(2), THREE_ROOM(3);

    private final int id;

    RoomQueryRoomType(int id) {
        this.id = id;
    }
}
