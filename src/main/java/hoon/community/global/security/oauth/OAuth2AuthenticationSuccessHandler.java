package hoon.community.global.security.oauth;

import hoon.community.domain.member.entity.Member;
import hoon.community.domain.member.repository.MemberRepository;
import hoon.community.domain.sign.service.SignService;
import hoon.community.domain.sign.service.TokenHelper;
import hoon.community.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenHelper accessTokenHelper;
    private final TokenHelper refreshTokenHelper;
    private final HttpSession session;

    private final MemberRepository memberRepository;

    private String defaultUrl = "/";
    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
        log.info("SUCCESS - User = {}", user);
        String email;
        if(user.getAttribute("email") != null) {
            email = user.getAttribute("email").toString();
        } else {
            email = user.getAttribute("id").toString();
        }


        Optional<Member> foundMember = memberRepository.findByEmail(email);

        Member member;
        String subject;
        String accessToken;
        String refreshToken;

        log.info("OAUTH Success Handler");

        if(foundMember.isPresent()) {
            member = foundMember.get();

            subject = String.valueOf(member.getId());
            accessToken = accessTokenHelper.createToken(subject);
            refreshToken = refreshTokenHelper.createToken(subject);

            /* 쿠키에 토큰 임의로 넣은 것 */
            Cookie cookie = new Cookie("accessToken", accessToken.substring(accessToken.lastIndexOf(" ")+1));
            Cookie cookie2 = new Cookie("refreshToken", refreshToken.substring(refreshToken.lastIndexOf(" ")+1));
            cookie.setHttpOnly(true);
            cookie2.setHttpOnly(true);
            cookie.setPath("/");
            cookie2.setPath("/");
            response.addCookie(cookie);
            response.addCookie(cookie2);

            /* 쿠키에 username 임의로 넣은 것 */
            Cookie userCookie = new Cookie("username", member.getUsername().split(" ")[0]);
            //username에 공백 들어가있는 경우가 있어서 공백기준 앞에 단어만 쿠키에 저장
            userCookie.setHttpOnly(true);
            userCookie.setPath("/");
            response.addCookie(userCookie);



        }

        resultRedirectStrategy(request, response, authentication);

    }

    protected void resultRedirectStrategy(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if(savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            log.info("TARGET URL = {}", targetUrl);
            redirectStrategy.sendRedirect(request, response, targetUrl);
        } else {
            redirectStrategy.sendRedirect(request, response, defaultUrl);
        }
    }
}
