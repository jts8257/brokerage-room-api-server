package com.tsjeong.brokerage.slice.service;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.config.location=classpath:/application-test.yml"
})
// Todo JwtConfig, TokenIssuer, JjWrTokenIssuer, TokenIssueService 를 읽어온다
// Todo UserRepository 를 Mock 으로 처리한다.
public class TokenIssueServiceTest {


}
