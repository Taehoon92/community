package hoon.community.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "400.badRequest"),

    BIND_ERROR(HttpStatus.BAD_REQUEST, "400.bindError"),

    SAME_AS_OLD_PASSWORD(HttpStatus.BAD_REQUEST, "400.sameAsOldPassword"),

    WRONG_OLD_PASSWORD(HttpStatus.BAD_REQUEST, "400.wrongOldPassword"),

    MISSING_REQUEST_HEADER(HttpStatus.BAD_REQUEST, "400.missingRequestHeader"),

    /* 401 UNAUTHORIZED */
    LOGIN_FAILURE(HttpStatus.UNAUTHORIZED, "401.loginFailure"),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "401.unauthorizedUser"),

    /* 403 FORBIDDEN */
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "403.accessDenied"),

    /* 404 NOT_FOUND */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "404.userNotFound"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "404.postNotFound"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "404.commentNotFound"),

    NOT_FOUND(HttpStatus.NOT_FOUND, "404.notFound"),

    FILE_UPLOAD_FAILURE(HttpStatus.NOT_FOUND, "404.fileUploadFailure"),

    /* 405 METHOD_NOT_ALLOWED */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "405.methodNotAllowed"),

    UNSUPPORTED_EXTENSION(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "415.unsupportedExtension"),

    /* 500 INTERNAL_SERVER_ERROR */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500.internalServerError"),

    CANNOT_CONVERT_NESTED_STRUCTURE(HttpStatus.INTERNAL_SERVER_ERROR, "500.cannotConvertNestedStructure"),

    ;

    private final HttpStatus status;
    private final String message;
}
