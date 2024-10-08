package com.tsjeong.brokerage.entity.room;

import com.tsjeong.brokerage.entity.BooleanToYNConverter;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "transaction_types")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class TransactionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(100)")
    private String name;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(columnDefinition = "VARCHAR(1)")
    private Boolean isDepositOnly;
}
