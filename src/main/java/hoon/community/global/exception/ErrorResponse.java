package hoon.community.global.exception;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.util.Locale;

@Getter
public class ErrorResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String code;
    private final String message;

    public ErrorResponse(ErrorCode errorCode, MessageSource messageSource, Locale locale) {
        this.status = errorCode.getStatus().value();
        this.error = errorCode.getStatus().name();
        this.code = errorCode.name();
//        this.message = errorCode.getMessage();
        this.message = messageSource.getMessage(errorCode.getMessage(), null, locale);
    }
}
