package com.tsjeong.brokerage.aop.aspect;

import com.tsjeong.brokerage.aop.annotation.JWT;
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
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

import static com.tsjeong.brokerage.service.token.issue.TokenIssueService.PAYLOAD_USER_ID_KEY;
import static com.tsjeong.brokerage.service.token.validate.TokenValidateService.TOKEN_PREFIX;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class TokenValidateAspect {

    private final TokenValidateService tokenValidateService;

    @Pointcut("@annotation(com.tsjeong.brokerage.aop.annotation.TokenValidate)")
    public void pointcut() {}

    @Around("pointcut() && @annotation(tokenValidate)")
    public Object checkAuthentication(ProceedingJoinPoint joinPoint, TokenValidate tokenValidate) throws Throwable {
        Method method = MethodFinder.findMethodBy(joinPoint);
        Parameter[] parameters = method.getParameters();
        Object[] args = joinPoint.getArgs();

        String token = getToken(parameters, args);

        Map<String, Object> payload = tokenValidateService.validateToken(token);

        if (tokenValidate.isUserIdInject()) {
            Integer argIndex = ArgIndexFinder.findIndexByAnnotation(parameters, UserIdInject.class);
            if (argIndex == null) {
                throw new IllegalArgumentException("토큰 payload 를 받을 파라미터가 누락되었습니다.");
            }
            args[argIndex] = payload.get(PAYLOAD_USER_ID_KEY);
        }

        return joinPoint.proceed(args);
    }

    private String getToken(Parameter[] parameters, Object[] args) {
        Integer tokenIndex = ArgIndexFinder.findIndexByAnnotation(parameters, JWT.class);
        String token;

        if (tokenIndex == null) {
            throw ErrorCode.TOKEN_NOT_EXISTS.build("토큰이 누락되었습니다.");
        }

        token = (String) args[tokenIndex];
        if (!token.startsWith(TOKEN_PREFIX)) {
            throw ErrorCode.INVALID_TOKEN.build("Bearer 토큰이 아닙니다.");
        }
        token = token.substring(TOKEN_PREFIX.length());
        return token;
    }
}
