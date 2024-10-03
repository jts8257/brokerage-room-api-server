package com.tsjeong.brokerage.entity.room;

import com.tsjeong.brokerage.entity.TimeStampBase;
import com.tsjeong.brokerage.entity.user.Users;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@Table(name = "rooms")
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id", callSuper = false)
public class Room extends TimeStampBase {

    @Id
    @GeneratedValue(generator = "tsid")
    @GenericGenerator(name = "tsid", strategy = "io.hypersistence.utils.hibernate.id.TsidGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private RoomType roomType;

    private String addressJibun;
    private String addressRoad;
    private String addressDetail;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detail_id")
    private RoomDetail detail;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<RoomTransaction> transactions;

    public List<RoomTransaction> getTransactions() {
        return Objects.isNull(transactions) ? new ArrayList<>() : transactions;
    }
}
