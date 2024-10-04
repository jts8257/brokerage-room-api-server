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

    private String name; // enum 이 아닌 DB 에 직접 글자를 넣어 관리함으로써 확장성을 확보

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isDepositOnly;
}
