package com.tsjeong.brokerage.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.*;
import java.util.Objects;

@Getter
@MappedSuperclass
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class TimeStampBase {

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public ZonedDateTime getCreatedAtKST() {
        if (Objects.isNull(createdAt)) {
            return null;
        }
        return createdAt.atZone(ZoneOffset.UTC)
                .withZoneSameInstant(ZoneId.of("Asia/Seoul")); // Convert to KST
    }

    public ZonedDateTime getUpdatedAtKST() {
        if (Objects.isNull(updatedAt)) {
            return null;
        }
        return updatedAt.atZone(ZoneOffset.UTC)
                .withZoneSameInstant(ZoneId.of("Asia/Seoul"));
    }

    public LocalDate getCreatedDateKST() {
        if (Objects.isNull(createdAt)) {
            return null;
        }
        return createdAt.atZone(ZoneOffset.UTC)
                .withZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .toLocalDate();
    }

    public LocalDate getUpdatedDateKST() {
        if (Objects.isNull(updatedAt)) {
            return null;
        }
        return updatedAt.atZone(ZoneOffset.UTC)
                .withZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .toLocalDate();
    }
}
