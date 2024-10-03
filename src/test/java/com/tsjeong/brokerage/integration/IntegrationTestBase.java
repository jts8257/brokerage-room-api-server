package com.tsjeong.brokerage.integration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.config.location=classpath:/application-test.yml"
})
public abstract class IntegrationTestBase {

}