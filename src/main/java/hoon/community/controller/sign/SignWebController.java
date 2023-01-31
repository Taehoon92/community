package hoon.community.controller.sign;

import hoon.community.domain.sign.dto.SignInRequest;
import hoon.community.domain.sign.dto.SignUpRequest;
import hoon.community.domain.sign.service.SignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SignWebController {

    private final SignService signService;

    @GetMapping("/sign-in")
    public String signIn() {
        return "sign/signIn";
    }

    @PostMapping("/sign-in")
    public String signIn(@Valid SignInRequest request, HttpServletResponse servletResponse) {
        signService.signIn(request, servletResponse);

        return "redirect:/";
    }


    @GetMapping("/sign-up")
    public String signUp(@ModelAttribute("member") SignUpRequest request) {
        return "sign/signUp";
    }

    @PostMapping("/sign-up")
    public String join(@Valid @ModelAttribute("member") SignUpRequest request, BindingResult bindingResult, HttpServletResponse servletResponse) {
        if (bindingResult.hasErrors()) {
            return "sign/signUp";
        }
        signService.signUp(request);

        //회원가입 후 자동 로그인
        SignInRequest signInRequest = SignUpRequest.toSignInRequest(request);
        signService.signIn(signInRequest, servletResponse);

        return "redirect:/";
    }

    @GetMapping("/sign-out")
    public String signOut(HttpServletRequest request, HttpServletResponse response) {
        //세션 삭제
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        //쿠키 삭제
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                cookies[i].setPath("/");
                cookies[i].setMaxAge(0);
                response.addCookie(cookies[i]);
            }
        }

        return "redirect:/";
    }
}
