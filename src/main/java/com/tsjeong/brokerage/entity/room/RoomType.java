package com.tsjeong.brokerage.entity.room;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "room_types")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name; // enum 이 아닌 DB 에 직접 글자를 넣어 관리함으로써 확장성을 확보
}
