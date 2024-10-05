package com.tsjeong.brokerage.entity.room;

import com.tsjeong.brokerage.entity.TimeStampBase;
import com.tsjeong.brokerage.entity.user.Users;

import com.tsjeong.brokerage.service.util.StringValidator;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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

    @Id
    @GeneratedValue(generator = "tsid")
    @GenericGenerator(name = "tsid", strategy = "io.hypersistence.utils.hibernate.id.TsidGenerator")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private RoomType roomType;

    private String addressJibun;
    private String addressRoad;
    private String addressDetail;

    @OneToOne(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH,CascadeType.REMOVE},
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "detail_id")
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
