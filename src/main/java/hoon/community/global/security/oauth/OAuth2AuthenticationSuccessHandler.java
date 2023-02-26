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
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
        String email = user.getAttribute("email").toString();
        Optional<Member> foundMember = memberRepository.findByEmail(email);

        Member member;
        String subject;
        String accessToken;
        String refreshToken;

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

            /* 세션에 username 임의로 넣은 것 */
            /*
            if(session != null) {
                session.setAttribute("username", member.getUsername());
            }

             */
        }

    }
}
