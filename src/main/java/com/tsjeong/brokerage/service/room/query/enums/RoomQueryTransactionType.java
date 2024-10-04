package com.tsjeong.brokerage.service.room.query.enums;

import lombok.Getter;

@Getter
public enum RoomQueryTransactionType {

    JEONSE(1), RENT(2);

    private final int id;

    RoomQueryTransactionType(int id) {
        this.id = id;
    }
}
