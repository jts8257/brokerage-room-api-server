package com.tsjeong.brokerage.entity.room;


import com.tsjeong.brokerage.entity.TimeStampBase;
import io.hypersistence.utils.hibernate.id.Tsid;
import lombok.*;

import jakarta.persistence.*;

@Getter
@Entity
@Table(name = "room_details")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id", callSuper = false)
public class RoomDetail extends TimeStampBase {

    @Id @Tsid
    private Long id;

    @OneToOne(mappedBy = "detail", fetch = FetchType.EAGER)
    private Room room;

    @Column(columnDefinition = "TEXT")
    private String description;

    public void update(String description) {
        this.description = description;
    }

    public void makeOrphan() {
        this.room = null;
    }
}
