package com.tsjeong.brokerage.entity.room;


import com.tsjeong.brokerage.entity.TimeStampBase;
import io.hypersistence.utils.hibernate.id.Tsid;
import lombok.*;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "room_transactions")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id", callSuper = false)
public class RoomTransaction extends TimeStampBase {

    @Id @Tsid
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transaction_type_id")
    private TransactionType transactionType;

    private BigDecimal rentMonthly;
    private BigDecimal deposit;

    @Transient
    public static int MAX_PER_ROOM = 2;

    public void update(
            BigDecimal rentMonthly,
            BigDecimal deposit
    ) {
        this.rentMonthly = rentMonthly;
        this.deposit = deposit;
    }
}
