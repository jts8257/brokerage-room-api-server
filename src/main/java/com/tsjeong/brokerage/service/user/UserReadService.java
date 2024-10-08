package com.tsjeong.brokerage.service.user;

import com.tsjeong.brokerage.dto.user.UserReadResponse;
import com.tsjeong.brokerage.entity.user.Users;
import com.tsjeong.brokerage.exception.ErrorCode;
import com.tsjeong.brokerage.repsoitory.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserReadService {

    private final UsersRepository usersRepository;

    @Transactional(readOnly = true)
    public List<UserReadResponse> getAllUsers() {
        List<Users> usersList = usersRepository.findAll();
        log.info(usersList.toString());
        return usersList.stream()
                .map(user -> new UserReadResponse(user.getId(), user.getNickName(), user.getEmail(), user.getPassword()))
                .toList();
    }

    public Users getUsersById(Long userId) {
        return usersRepository.findById(userId).orElseThrow(() -> ErrorCode.USER_NOT_EXISTS.build("'id:%d'에 해당하는 유저를 찾을 수 없습니다."));
    }
}
