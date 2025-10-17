package com.zepox.EcommerceWebApp.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogError {
    private LocalDateTime timestamp;
    private String exceptionType;
    private String message;
    private String sourceClass;
    private String method;
    private int lineNumber;

    public LogError() {
        this.timestamp = LocalDateTime.now();
    }
    public LogError(String exceptionType, String message,
                    String sourceClass, String method, int lineNumber) {
        this();
        this.exceptionType = exceptionType;
        this.message = message;
        this.sourceClass = sourceClass;
        this.method = method;
        this.lineNumber = lineNumber;
    }
}
