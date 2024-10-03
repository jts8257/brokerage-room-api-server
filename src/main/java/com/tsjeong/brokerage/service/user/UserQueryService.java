package com.tsjeong.brokerage.service.user;

import com.tsjeong.brokerage.dto.user.UserReadResponse;
import com.tsjeong.brokerage.repsoitory.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UsersRepository usersRepository;


    public List<UserReadResponse> getAllUsers() {
        return usersRepository.findAll().stream()
                .map(user -> new UserReadResponse(user.getId(), user.getEmail(), user.getPassword()))
                .toList();
    }

}
