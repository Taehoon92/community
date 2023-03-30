package hoon.community.global.security;

import com.querydsl.core.types.dsl.BooleanExpression;
import hoon.community.domain.role.entity.RoleType;
import hoon.community.domain.sign.service.TokenHelper;
import hoon.community.domain.sign.service.TokenService;
import hoon.community.global.security.guard.AuthHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
//@Component
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final TokenHelper accessTokenHelper;
    private final CustomUserDetailsService userDetailsService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        String token;

        /**
         * View 용 쿠키에 임시 저장된 토큰값 가져오기
         */
        Cookie cookie = null;
        Cookie userCookie = null;
        String username = "";

        if(((HttpServletRequest) request).getCookies() != null) {
            cookie = Arrays.stream(((HttpServletRequest) request).getCookies()).filter(c -> c.getName().equals("accessToken")).findAny().orElse(null);
            userCookie = Arrays.stream(((HttpServletRequest) request).getCookies()).filter(c -> c.getName().equals("username")).findAny().orElse(null);
            if(userCookie != null) username = userCookie.getValue();
        }

        
        if(cookie != null) {
            token = "Bearer " + cookie.getValue(); // 쿠키에서 accessToken 가져오기 위해서 임의로 작성한 코드
        }
        else {
            token = extractToken(request);
        }



        if(validateToken(token)) {
            setAuthentication(token);

            setSession(request, username);
        }

        chain.doFilter(request, response);
    }

    private String extractToken(ServletRequest request) {
        return ((HttpServletRequest) request).getHeader("Authorization");
    }

    private boolean validateToken(String token) {
        return token != null && accessTokenHelper.validate(token);
    }

    private void setAuthentication(String token) {
        String userId = accessTokenHelper.extractSubject(token);
        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        SecurityContextHolder.getContext().setAuthentication(new CustomAuthenticationToken(userDetails, userDetails.getAuthorities()));
    }

    //사용자 정보용 세션 set
    private void setSession(ServletRequest request, String username) {
        HttpSession session = ((HttpServletRequest)request).getSession();

        Optional userRole;
        userRole = AuthHelper.extractMemberRoles().stream().filter(roleType -> roleType == RoleType.ROLE_ADMIN).findAny();

        Enumeration<?> attrName = session.getAttributeNames();

        session.setAttribute("username", username);

        if (userRole.isPresent()) {
            session.setAttribute("role", "Admin");
        }
        else {
            session.setAttribute("role", "User");
        }
    }

}
