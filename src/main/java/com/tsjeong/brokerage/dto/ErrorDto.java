package com.tsjeong.brokerage.dto;

import com.tsjeong.brokerage.exception.ApplicationException;
import lombok.Getter;

@Getter
public class ErrorDto {
    private String code;
    private int statusCode;
    private String message;

    private ErrorDto() {}

    public static ErrorDto of(ApplicationException ape) {
        ErrorDto errorContainer = new ErrorDto();
        errorContainer.code = ape.getCode();
        errorContainer.statusCode = ape.getHttpStatus().value();
        errorContainer.message = ape.getReason();
        return errorContainer;
    }
}
