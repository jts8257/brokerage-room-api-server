package com.tsjeong.brokerage.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String code;
    private final String reason;


    public ApplicationException(HttpStatus httpStatus, String code, String reason) {
        super(reason);
        this.httpStatus = httpStatus;
        this.code = code;
        this.reason = reason;
    }

}
