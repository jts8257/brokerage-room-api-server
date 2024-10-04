package com.tsjeong.brokerage.entity.room;


import com.tsjeong.brokerage.entity.TimeStampBase;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "room_details")
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id", callSuper = false)
public class RoomDetail extends TimeStampBase {

    @Id
    @GeneratedValue(generator = "tsid")
    @GenericGenerator(name = "tsid", strategy = "io.hypersistence.utils.hibernate.id.TsidGenerator")
    private Long id;

    @OneToOne(mappedBy = "detail", fetch = FetchType.EAGER)
    private Room room;

    @Column(columnDefinition = "TEXT")
    private String description;

    public void update(String description) {
        this.description = description;
    }
}
