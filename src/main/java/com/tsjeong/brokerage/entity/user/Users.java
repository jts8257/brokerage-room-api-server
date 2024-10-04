package com.tsjeong.brokerage.entity.user;

import com.tsjeong.brokerage.entity.room.Room;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Builder
public class Users {

    @Id
    @GeneratedValue(generator = "tsid")
    @GenericGenerator(name = "tsid", strategy = "io.hypersistence.utils.hibernate.id.TsidGenerator")
    private Long id;
    private String nickName;
    private String email;
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Room> rooms;


    public List<Room> getRooms() {
        return Objects.isNull(rooms) ? new ArrayList<>() : rooms;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", nickName='" + nickName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
