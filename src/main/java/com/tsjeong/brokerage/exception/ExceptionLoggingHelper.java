package com.tsjeong.brokerage.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

public class ExceptionLoggingHelper {
    private static final Logger log = LoggerFactory.getLogger(ExceptionLoggingHelper.class);


    public static void log(
            Throwable e,
            @NotNull HttpServletRequest request,
            HttpStatus httpStatus
    ) {
        if (e != null) {
            log.error("[{}][{}]:[{}] - {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    httpStatus.value(),
                    e.getMessage()
            );
            log.error("Full stacktrace:", e);
        }
    }
}
