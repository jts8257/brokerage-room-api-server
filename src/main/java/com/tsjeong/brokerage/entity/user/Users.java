package com.tsjeong.brokerage.entity.user;

import com.tsjeong.brokerage.entity.room.Room;
import io.hypersistence.utils.hibernate.id.Tsid;
import lombok.*;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Builder
public class Users {

    @Id @Tsid
    private Long id;
    private String nickName;
    private String email;
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Room> rooms;


    public List<Room> getRooms() {
        return Objects.isNull(rooms) ? new ArrayList<>() : rooms;
    }
}
