package com.tsjeong.brokerage.exception;

import com.tsjeong.brokerage.dto.ResponseDto;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.tsjeong.brokerage.exception.ErrorCode.*;


@RestControllerAdvice
public class ApplicationAdvice {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ResponseDto<?>> applicationExceptionHandler(
            HttpServletRequest request,
            ApplicationException e
    ) {

        ExceptionLoggingHelper.log(e, request, e.getHttpStatus());

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ResponseDto.error(e));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDto<?>> messageNotReadableExceptionHandler(
            HttpServletRequest request,
            HttpMessageNotReadableException e
    ) {

        ExceptionLoggingHelper.log(e, request, HttpStatus.BAD_REQUEST);

        return ResponseEntity
                .badRequest()
                .body(ResponseDto.error(BAD_BODY.build("요청의 body 부분이 누락되었거나 enum type 오류가 있습니다.")));

    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<?>> responseValidationExceptionHandler(
            HttpServletRequest request,
            MethodArgumentNotValidException e
    ) {

        boolean filedError = true;

        List<String> reasons = e.getFieldErrors().stream()
                .map(fieldError -> "field [%s] : %s".formatted(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        if (reasons.isEmpty()) {
            filedError = false;
            reasons = e.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
        }

        ApplicationException ape = filedError ? BAD_CONSTRAINT_BODY_FIELD.build(reasons.toString()) : BAD_CONSTRAINT_OTHER.build(reasons.toString());
        ExceptionLoggingHelper.log(e, request, ape.getHttpStatus());

        return ResponseEntity
                .badRequest()
                .body(ResponseDto.error(ape));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestHeaderException.class)
    public  ResponseEntity<ResponseDto<?>> missingRequestHeaderExceptionHandler(
            HttpServletRequest request,
            MissingRequestHeaderException e
    ) {

        ExceptionLoggingHelper.log(e, request, BAD_HEADER.getHttpStatus());

        String missingHeader = e.getHeaderName();

        ApplicationException ape;
        if (Objects.equals(HttpHeaders.AUTHORIZATION, missingHeader)) {
            ape = TOKEN_NOT_EXISTS.build("헤더 '%s' 가 누락되었습니다.".formatted(HttpHeaders.AUTHORIZATION));
        } else {
            ape = BAD_HEADER.build("헤더 '%s' 가 누락되었습니다.".formatted(missingHeader));
        }


        return ResponseEntity
                .status(ape.getHttpStatus())
                .body(ResponseDto.error(ape));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseDto<?>> MethodArgumentTypeMismatchExceptionHandler(
            HttpServletRequest request,
            MethodArgumentTypeMismatchException e
    ) {
        ExceptionLoggingHelper.log(e, request, BAD_PARAMETER_TYPE.getHttpStatus());


        return ResponseEntity
                .badRequest()
                .body(ResponseDto.error(BAD_PARAMETER_TYPE.build("'%s'의 타입을 확인해주세요.".formatted(e.getName()))));

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResponseDto<?>> missingServletRequestParameterExceptionHandler(
            HttpServletRequest request,
            MissingServletRequestParameterException e
    ) {

        ExceptionLoggingHelper.log(e, request, BAD_PARAMETER.getHttpStatus());

        return ResponseEntity
                .badRequest()
                .body(ResponseDto.error(BAD_PARAMETER.build("요청 파라미터 '%s' 가 누락되었습니다.".formatted(e.getParameterName()))));

    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseDto<?>> notSupportedMethodExceptionHandler(
            HttpServletRequest request,
            HttpRequestMethodNotSupportedException e
    ) {

        ExceptionLoggingHelper.log(e, request, BAD_METHOD.getHttpStatus());

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ResponseDto.error(BAD_METHOD.build("'%s' 접근은 허용되지 않았습니다.".formatted(e.getMethod()))));

    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ResponseDto<?>> throwableHandler(
            HttpServletRequest request,
            Throwable e
    ) {

        ExceptionLoggingHelper.log(e, request, UNEXPECTED.getHttpStatus());

        return ResponseEntity
                .internalServerError()
                .body(ResponseDto.error(UNEXPECTED.build()));
    }

}
