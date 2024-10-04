package com.tsjeong.brokerage.slice.controller;

import com.tsjeong.brokerage.exception.ApplicationAdvice;
import com.tsjeong.brokerage.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(ApplicationAdvice.class)
@ContextConfiguration(classes = ApplicationAdviceTest.DummyController.class)
class ApplicationAdviceTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    @DisplayName("DummyController - Invalid Token Exception")
    void shouldReturnUnauthorizedWhenInvalidTokenExceptionThrown() throws Exception {
        mockMvc.perform(get("/dummy/token-invalid"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error.code").value("AC-0002"));
    }

    @Test
    @DisplayName("DummyController - Authorization Header Missing Exception")
    void shouldReturnUnAuthorizedWhenAuthorizationHeaderMissing() throws Exception {
        mockMvc.perform(get("/dummy/authorization-missing"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error.code").value("AC-0005"));
    }

    @Test
    @DisplayName("DummyController - Non Authorization Header Missing Exception")
    void shouldReturnBadRequestWhenAuthorizationHeaderMissing() throws Exception {
        mockMvc.perform(get("/dummy/header-missing"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("BR-0001"));
    }

    @Test
    @DisplayName("DummyController - Request Parameter Type Mismatch Exception")
    void shouldReturnBadRequestWhenParameterMismatch() throws Exception {
        mockMvc.perform(get("/dummy/param-mismatch")
                        .param("number", "invalid")) // 파라미터에 잘못된 값 전달
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("BR-0007"));
    }

    @RestController
    @RequestMapping("/dummy")
    public static class DummyController {

        @GetMapping("/token-invalid")
        public void throwInvalidTokenException() {
            throw ErrorCode.INVALID_TOKEN.build();
        }

        @GetMapping("/header-missing")
        public void throwMissingHeaderException(@RequestHeader("Hat") String hat) {

        }

        @GetMapping("/authorization-missing")
        public void throwTokenNotExistsException(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {

        }

        @GetMapping("/param-mismatch")
        public void throwParameterMismatchException(@RequestParam(required = true) int number) {
            // number 파라미터가 int로 변환할 수 없는 값일 경우 MethodArgumentTypeMismatchException이 발생합니다.
        }
    }
}