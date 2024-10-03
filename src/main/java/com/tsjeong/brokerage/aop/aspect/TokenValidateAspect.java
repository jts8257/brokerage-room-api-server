package com.tsjeong.brokerage.aop.aspect;

import com.tsjeong.brokerage.aop.util.ArgIndexFinder;
import com.tsjeong.brokerage.aop.util.MethodFinder;
import com.tsjeong.brokerage.aop.annotation.TokenValidate;
import com.tsjeong.brokerage.aop.annotation.UserIdInject;
import com.tsjeong.brokerage.exception.ErrorCode;
import com.tsjeong.brokerage.service.token.validate.TokenValidateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Objects;

import static com.tsjeong.brokerage.service.token.issue.TokenIssueService.PAYLOAD_USER_ID_KEY;
import static com.tsjeong.brokerage.service.token.validate.TokenValidateService.TOKEN_PREFIX;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class TokenValidateAspect {

    private final TokenValidateService tokenValidateService;
    private final HttpServletRequest request;

    @Pointcut("@annotation(com.tsjeong.brokerage.aop.annotation.TokenValidate)")
    public void pointcut() {}

    @Around("pointcut() && @annotation(tokenValidate)")
    public Object checkAuthentication(ProceedingJoinPoint joinPoint, TokenValidate tokenValidate) throws Throwable {

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Objects.isNull(token)) {
            throw ErrorCode.TOKEN_NOT_EXISTS.build("Authorization 헤더가 누락되었습니다.");
        }

        if (!token.startsWith(TOKEN_PREFIX)) {
            throw ErrorCode.INVALID_TOKEN.build("Bearer 토큰이 아닙니다.");
        }

        token = token.substring(TOKEN_PREFIX.length());

        Map<String, Object> payload = tokenValidateService.validateToken(token);

        Method method = MethodFinder.findMethodBy(joinPoint);
        Object[] args = joinPoint.getArgs();
        Parameter[] parameters = method.getParameters();

        Integer argIndex = ArgIndexFinder.findIndexByAnnotation(parameters, UserIdInject.class);
        if (argIndex == null) {
            throw new IllegalArgumentException("토큰 payload 를 받을 파라미터가 누락되었습니다.");
        }
        args[argIndex] = payload.get(PAYLOAD_USER_ID_KEY);

        return joinPoint.proceed(args);
    }
}
