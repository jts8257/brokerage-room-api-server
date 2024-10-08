package com.tsjeong.brokerage.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_TOKEN_PROVIDE_PARAM(HttpStatus.INTERNAL_SERVER_ERROR, "AC-0001", "Error Occurs while Creating Authentication Method"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AC-0002", "Invalid authentication Method"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "AC-0003", "Password is not matched"),
    TOKEN_NOT_EXISTS(HttpStatus.UNAUTHORIZED, "AC-0004", "The %s Header is Missing".formatted(HttpHeaders.AUTHORIZATION)),
    USER_NOT_ACCESSIBLE(HttpStatus.FORBIDDEN, "AC-0005", "The user exists but is not registered for your service"),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AC-0006", "The access token expired"),


    USER_NOT_EXISTS(HttpStatus.NOT_FOUND, "EN-0001", "The user not found"),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "EN-0002", "The Entity required is Not exists"),
    ENTITY_REGISTER_CONFLICT(HttpStatus.CONFLICT, "EN-0003", "The Entity is not qualified database or business constrains"),

    MISSING_REQUIRED_RELATION(HttpStatus.INTERNAL_SERVER_ERROR, "EN-0004", "The Entity miss its required relation"),
    ENTITY_READ_CONFLICT(HttpStatus.INTERNAL_SERVER_ERROR, "EN-0005", "The Entity violate business constraints"),

    BAD_HEADER(HttpStatus.BAD_REQUEST, "BR-0001", "the required http header is missing"),
    BAD_PARAMETER(HttpStatus.BAD_REQUEST, "BR-0002", "the required http query param is missing"),
    BAD_BODY(HttpStatus.BAD_REQUEST, "BR-0003", "the required http body is missing"),

    BAD_CONSTRAINT_BODY_FIELD(HttpStatus.BAD_REQUEST, "BR-0004", "the request body filed value doesn't fit required constraint"),
    BAD_CONSTRAINT_OTHER(HttpStatus.BAD_REQUEST, "BR-0005", "the request doesn't fit required constraint"),
    BAD_METHOD(HttpStatus.METHOD_NOT_ALLOWED, "BR-0006", "the method not allowed"),
    BAD_PARAMETER_TYPE(HttpStatus.BAD_REQUEST, "BR-0007", "the request parameter violate type constraint"),

    UNEXPECTED(HttpStatus.INTERNAL_SERVER_ERROR, "IS-0001", "unexpected exception occurs");


    private final HttpStatus httpStatus;
    private final String code;
    private final String reason;

    public ApplicationException build() {
        return new ApplicationException(httpStatus, code, reason);
    }

    public ApplicationException build(String reason) {return new ApplicationException(httpStatus, code, reason);}
}
