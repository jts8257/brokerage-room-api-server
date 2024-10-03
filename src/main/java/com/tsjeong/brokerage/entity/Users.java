package com.tsjeong.brokerage.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Users {

    @Id
    private Integer id;
    private String nickName;
    private String email;
    private String password;
}
