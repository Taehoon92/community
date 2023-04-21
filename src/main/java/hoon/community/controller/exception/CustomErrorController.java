package hoon.community.controller.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler(OAuthProviderMissMatchException.class)
    public String handleOAuthProviderMissMatchException(OAuthProviderMissMatchException ex) {
        log.info("에러 - 에러 컨트롤러 = {}", ex);
        return "redirect:/auth/sign-in?error="+ex.getMessage();

    }
}
