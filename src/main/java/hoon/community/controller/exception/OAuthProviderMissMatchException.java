package hoon.community.controller.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OAuthProviderMissMatchException extends RuntimeException{

    public OAuthProviderMissMatchException(String message) {
        super(message);
    }
}
