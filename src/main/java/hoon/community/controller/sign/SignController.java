package hoon.community.controller.sign;

import hoon.community.controller.response.Response;
import hoon.community.domain.sign.dto.DuplicateEmailCheckRequest;
import hoon.community.domain.sign.dto.SignInRequest;
import hoon.community.domain.sign.dto.SignUpRequest;
import hoon.community.domain.sign.service.SignService;
import hoon.community.global.exception.CustomException;
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

    @ApiOperation(value = "Sign up", notes = "Sign up request")
    @PostMapping("/api/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public Response signUp(@Valid @RequestBody SignUpRequest request, BindingResult bindingResult, HttpServletResponse servletResponse) {
        if(bindingResult.hasErrors()) {
            return Response.failure(0, bindingResult.getFieldError().getDefaultMessage());
        }
        if(signService.duplicateEmailCheck(request.getEmail())){
            return Response.failure(0, "There is duplicate email.");
        }
        signService.signUp(request);
        SignInRequest signInRequest = new SignInRequest(request.getEmail(), request.getPassword());
        return Response.success(signService.signIn(signInRequest, servletResponse));
    }

    @ApiOperation(value = "Sign in", notes = "Sign in request")
    @PostMapping("/api/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public Response signIn(@Valid @RequestBody SignInRequest request, HttpServletResponse servletResponse) {
        return Response.success(signService.signIn(request, servletResponse));
    }

    @ApiOperation(value = "Reissue access token", notes = "Reissue access token using refresh token")
    @PostMapping("/api/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public Response refreshToken(@RequestHeader(value = "Authorization") String refreshToken) {
        return Response.success(signService.refreshToken(refreshToken));
    }

    @ApiOperation(value = "Email duplicate check", notes = "Email duplicate check")
    @PostMapping("/api/duplicate-email-check")
    @ResponseStatus(HttpStatus.OK)
    public Response duplicateEmailCheck(@Valid @RequestBody DuplicateEmailCheckRequest request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return Response.failure(0, bindingResult.getFieldError().getDefaultMessage());
        }
        if(signService.duplicateEmailCheck(request.getEmail())){
            return Response.failure(0, "There is duplicate email.");
        }
        return Response.success(true);
    }
}
