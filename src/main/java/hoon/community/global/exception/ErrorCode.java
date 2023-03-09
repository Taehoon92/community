package hoon.community.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    BIND_ERROR(HttpStatus.BAD_REQUEST, "바인딩 오류입니다."),

    SAME_AS_OLD_PASSWORD(HttpStatus.BAD_REQUEST, "이전 비밀번호와 현재 비밀번호가 같습니다."),

    WRONG_OLD_PASSWORD(HttpStatus.BAD_REQUEST, "현재 비밀번호가 일치하지 않습니다."),

    MISSING_REQUEST_HEADER(HttpStatus.BAD_REQUEST, "요청 헤더가 누락되었습니다"),

    /* 401 UNAUTHORIZED */
    LOGIN_FAILURE(HttpStatus.UNAUTHORIZED, "로그인 정보가 잘못되었습니다."),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),

    /* 403 FORBIDDEN */
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "액세스가 거부되었습니다."),

    /* 404 NOT_FOUND */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저 정보를 찾을 수 없습니다."),
    ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "권한 정보를 찾을 수 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글 정보를 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글 정보를 찾을 수 없습니다."),

    NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 정보를 찾을 수 없습니다."),

    FILE_UPLOAD_FAILURE(HttpStatus.NOT_FOUND, "파일 업로드에 실패하였습니다."),

    /* 405 METHOD_NOT_ALLOWED */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메서드입니다."),

    UNSUPPORTED_EXTENSION(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원하지 않는 확장자입니다."),

    /* 500 INTERNAL_SERVER_ERROR */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다."),

    CANNOT_CONVERT_NESTED_STRUCTURE(HttpStatus.INTERNAL_SERVER_ERROR, "중첩 구조 변환에 실패하였습니다."),

    ;

    private final HttpStatus status;
    private final String message;
}
