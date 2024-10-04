package com.tsjeong.brokerage.integration;

import com.tsjeong.brokerage.entity.room.RoomType;
import com.tsjeong.brokerage.entity.room.TransactionType;
import com.tsjeong.brokerage.entity.user.Users;
import com.tsjeong.brokerage.repsoitory.room.RoomTypeRepository;
import com.tsjeong.brokerage.repsoitory.room.TransactionTypeRepository;
import com.tsjeong.brokerage.repsoitory.user.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public abstract class IntegrationTestBase {
    @Autowired
    protected UsersRepository usersRepository;

    @Autowired
    protected RoomTypeRepository roomTypeRepository;

    @Autowired
    protected TransactionTypeRepository transactionTypeRepository;

    protected long user1Id;
    protected long user2Id;
    protected int jeonseId;
    protected int rentId;
    protected int oneRoomId;
    protected int twoRoomId;
    protected int threeRoomId;
    @BeforeEach
    void setUp() {
        // 테스트 데이터가 이미 존재하지 않는 경우에만 삽입
        if (usersRepository.count() == 0) {
            Users user1 = usersRepository.save(Users.builder()
                    .nickName("홍길동")
                    .email("hong@naver.com")
                    .password("1234")
                    .build());
            user1Id = user1.getId();

            Users user2 = usersRepository.save(Users.builder()
                    .nickName("김철수")
                    .email("kim@naver.com")
                    .password("1234")
                    .build());
            user2Id = user2.getId();
        }

        if (transactionTypeRepository.count() == 0) {
            TransactionType jeonse = transactionTypeRepository.save(
                    TransactionType.builder()
                    .name("전세")
                    .isDepositOnly(true)
                    .build());
            jeonseId = jeonse.getId();

            TransactionType monthlyRent = transactionTypeRepository.save(
                    TransactionType.builder()
                    .name("월세")
                    .isDepositOnly(false)
                    .build());
            rentId = monthlyRent.getId();

        }

        if (roomTypeRepository.count() == 0) {

            RoomType oneRoom = roomTypeRepository.save(RoomType.builder().name("원룸").build());
            RoomType twoRoom = roomTypeRepository.save(RoomType.builder().name("투룸").build());
            RoomType threeRoom = roomTypeRepository.save(RoomType.builder().name("쓰리룸").build());

            oneRoomId = oneRoom.getId();
            twoRoomId = twoRoom.getId();
            threeRoomId = threeRoom.getId();
        }
    }
}