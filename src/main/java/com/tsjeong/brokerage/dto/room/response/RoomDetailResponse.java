package com.tsjeong.brokerage.dto.room.response;

import java.time.ZonedDateTime;
import java.util.List;


public record RoomDetailResponse(
        String id,
        Integer roomTypeId,
        String roomTypeName,
        String addressJibun,
        String addressRoad,
        String addressDetail,
        List<RoomTransactionResponse> transactions,
        ZonedDateTime postedAt,
        ZonedDateTime updatedAt,
        String userName,
        String description,
        Boolean isEditable,
        Boolean isDeletable
) {}