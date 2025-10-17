package com.zepox.EcommerceWebApp.util;

import com.zepox.EcommerceWebApp.exception.LogError;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Deprecated // for the time being.. Will think later about this shit.
@Component
public class LogErrorDetails {
    public void logTheErrorDetails(Logger logger, Exception ex) {
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
}
