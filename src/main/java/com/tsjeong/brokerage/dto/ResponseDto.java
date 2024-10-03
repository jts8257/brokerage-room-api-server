package com.tsjeong.brokerage.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tsjeong.brokerage.exception.ApplicationException;
import lombok.Data;

@Data
public class ResponseDto<T> {
    private ErrorDto error;
    private T item;

    @JsonIgnore
    public static <T> ResponseDto<T> success(T item) {
        ResponseDto<T> dto = new ResponseDto<>();
        dto.error = null;
        dto.item = item;
        return dto;
    }

    @JsonIgnore
    public static <T> ResponseDto<T> error(ApplicationException ape) {
        ResponseDto<T> dto = new ResponseDto<>();
        dto.error = ErrorDto.of(ape);
        dto.item = null;
        return dto;
    }

    @JsonIgnore
    public static <T> ResponseDto<T> empty() {
        ResponseDto<T> dto = new ResponseDto<>();
        dto.error = null;
        dto.item = null;
        return dto;
    }
}
