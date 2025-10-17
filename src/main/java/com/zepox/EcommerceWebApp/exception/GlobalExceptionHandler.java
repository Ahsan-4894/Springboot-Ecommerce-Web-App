package com.zepox.EcommerceWebApp.exception;

import com.zepox.EcommerceWebApp.util.LogErrorDetails;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;


@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    public void logTheErrorDetails(Exception ex) {
        StackTraceElement source = Arrays
                .stream(ex.getStackTrace())
                .filter(el->el.getClassName().startsWith("com.zepox"))
                .findFirst()
                .orElse(ex.getStackTrace()[0]);
        LogError logError = new LogError(
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                source.getClassName(),
                source.getMethodName(),
                source.getLineNumber()
        );

        logger.error(
                "Error [{}] at {}.{}(line {}): {}",
                logError.getExceptionType(),
                logError.getSourceClass(),
                logError.getMethod(),
                logError.getLineNumber(),
                logError.getMessage()
        );
    }

//    private static LogErrorDetails logErrorDetails;

    @ExceptionHandler(UsernameNotFoundException.class)
    ResponseEntity<ApiError> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        logTheErrorDetails(ex);
        ApiError apiError = new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(apiError, apiError.getStatusCode());
    }
}
