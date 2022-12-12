package hoon.community.controller.exception;

import hoon.community.global.exception.CustomException;
import hoon.community.global.exception.ErrorCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exception")
public class ExceptionController {

    @GetMapping("/entry-point")
    public void entryPoint() {
        throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
    }

    @GetMapping("/access-denied")
    public void accessDenied() {
        throw new CustomException(ErrorCode.ACCESS_DENIED);
    }
}
