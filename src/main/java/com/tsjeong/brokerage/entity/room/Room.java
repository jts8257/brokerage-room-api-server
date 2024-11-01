package com.tsjeong.brokerage.entity.room;

import com.tsjeong.brokerage.entity.TimeStampBase;
import com.tsjeong.brokerage.entity.user.Users;

import com.tsjeong.brokerage.service.util.StringValidator;
import io.hypersistence.utils.hibernate.id.Tsid;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@Table(name = "rooms")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id", callSuper = false)
public class Room extends TimeStampBase {

    @Id @Tsid
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "BIGINT")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", columnDefinition = "INT")
    private RoomType roomType;

    @Column(columnDefinition = "VARCHAR(200)")
    private String addressJibun;
    @Column(columnDefinition = "VARCHAR(200)")
    private String addressRoad;
    @Column(columnDefinition = "VARCHAR(200)")
    private String addressDetail;

    @OneToOne(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH,CascadeType.REMOVE},
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "detail_id", columnDefinition = "BIGINT")
    private RoomDetail detail;

    @OneToMany(
            mappedBy = "room",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE},
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @BatchSize(size = 100)
    private List<RoomTransaction> transactions;

    public List<RoomTransaction> getTransactions() {
        if (Objects.isNull(transactions)) {
            this.transactions = new ArrayList<>();
        }
        return transactions;
    }

    public void update(
            RoomType roomType,
            String addressJibun,
            String addressRoad,
            String addressDetail
    ) {
        if (!Objects.isNull(roomType)) {
            this.roomType = roomType;
        }

        this.addressJibun = addressJibun;
        this.addressRoad = addressRoad;
        this.addressDetail = addressDetail;
    }

    public void updateDetail(String description) {
        if (!StringValidator.isEmptyOrNull(description) && detail != null) {
            detail.update(description);

        } else if (!StringValidator.isEmptyOrNull(description) && detail == null) {
            detail = RoomDetail.builder()
                    .description(description)
                    .build();
        }
    }

    public void deleteDetail() {
        if (detail != null) {
            detail.makeOrphan();
            this.detail = null;
        }
    }
}
