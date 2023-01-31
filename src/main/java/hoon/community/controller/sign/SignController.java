package hoon.community.controller.sign;

import hoon.community.controller.response.Response;
import hoon.community.domain.sign.dto.DuplicateEmailCheckRequest;
import hoon.community.domain.sign.dto.SignInRequest;
import hoon.community.domain.sign.dto.SignUpRequest;
import hoon.community.domain.sign.service.SignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Api(value = "Sign Controller", tags = "Sign")
@RestController
@RequiredArgsConstructor
public class SignController {

    private final SignService signService;

    @ApiOperation(value = "회원가입", notes = "회원가입을 한다.")
    @PostMapping("/api/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public Response signUp(@Valid @RequestBody SignUpRequest request) {
        signService.signUp(request);
        return Response.success();
    }

    @ApiOperation(value = "로그인", notes = "로그인을 한다.")
    @PostMapping("/api/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public Response signIn(@Valid @RequestBody SignInRequest request, HttpServletResponse servletResponse) {
        return Response.success(signService.signIn(request, servletResponse));
    }

    @ApiOperation(value = "토큰 재발급", notes = "Refresh Token으로 새로운 Access Token을 발급받는다.")
    @PostMapping("/api/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public Response refreshToken(@RequestHeader(value = "Authorization") String refreshToken) {
        return Response.success(signService.refreshToken(refreshToken));
    }

    @PostMapping("/api/duplicate-email-check")
    @ResponseStatus(HttpStatus.OK)
    public Response duplicateEmailCheck(@Valid @RequestBody DuplicateEmailCheckRequest request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return Response.failure(0, bindingResult.getFieldError().getDefaultMessage());
        }
        if(signService.duplicateEmailCheck(request.getEmail())){
            return Response.failure(0, "이미 존재하는 이메일입니다.");
        }
        return Response.success(true);
    }
}
