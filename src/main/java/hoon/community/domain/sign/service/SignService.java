package hoon.community.domain.sign.service;

import hoon.community.domain.member.entity.*;
import hoon.community.domain.member.repository.MemberRepository;
import hoon.community.domain.role.entity.Role;
import hoon.community.domain.role.repository.RoleRepository;
import hoon.community.domain.role.entity.RoleType;
import hoon.community.domain.sign.dto.RefreshTokenResponse;
import hoon.community.domain.sign.dto.SignInRequest;
import hoon.community.domain.sign.dto.SignInResponse;
import hoon.community.domain.sign.dto.SignUpRequest;
import hoon.community.global.exception.CustomException;
import hoon.community.global.exception.ErrorCode;
import hoon.community.global.security.guard.AuthHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenHelper accessTokenHelper;
    private final TokenHelper refreshTokenHelper;

    private final HttpSession session;

    @Transactional
    public void signUp(SignUpRequest req) {
        validateSignUpInfo(req);
        List<Role> roles = List.of(roleRepository.findByRoleType(RoleType.ROLE_USER).orElseThrow(() -> new CustomException(ErrorCode.ROLE_NOT_FOUND)));
        memberRepository.save(SignUpRequest.toEntity(req, roles, passwordEncoder));
    }

    @Transactional(readOnly = true)
    public SignInResponse signIn(SignInRequest req, HttpServletResponse servletResponse) {
        Member member = memberRepository.findByEmail(req.getEmail()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        validatePassword(req, member);
        String subject = createSubject(member);
        String accessToken = accessTokenHelper.createToken(subject);
        String refreshToken = refreshTokenHelper.createToken(subject);

        /* 쿠키에 토큰 임의로 넣은 것 */
        Cookie cookie = new Cookie("accessToken", accessToken.substring(accessToken.lastIndexOf(" ")+1));
        Cookie cookie2 = new Cookie("refreshToken", refreshToken.substring(refreshToken.lastIndexOf(" ")+1));
        cookie.setHttpOnly(true);
        cookie2.setHttpOnly(true);
        cookie.setPath("/");
        cookie2.setPath("/");
        servletResponse.addCookie(cookie);
        servletResponse.addCookie(cookie2);

        /* 세션에 username 임의로 넣은 것 */
        setSessionMemberInfo(member);

        return new SignInResponse(accessToken, refreshToken);
    }


    public boolean duplicateEmailCheck(String email) {
        return memberRepository.existsByEmail(email);
    }

    public RefreshTokenResponse refreshToken(String refreshToken) {
        validateRefreshToken(refreshToken);
        String subject = refreshTokenHelper.extractSubject(refreshToken);
        String accessToken = accessTokenHelper.createToken(subject);
        return new RefreshTokenResponse(accessToken);
    }

    private void validateSignUpInfo(SignUpRequest req) {
        if(memberRepository.existsByEmail(req.getEmail())) {
            throw new CustomException(ErrorCode.BAD_REQUEST); //이메일 중복 커스텀 오류 만들기
            //throw new MemberEmailAlreadyExistsException(req.getEmail());
        }
    }

    private void validatePassword(SignInRequest req, Member member) {
        if (!passwordEncoder.matches(req.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAILURE);
        }
    }

    private void validateRefreshToken(String refreshToken) {
        if (!refreshTokenHelper.validate(refreshToken)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }
    }

    private String createSubject(Member member) {
        return String.valueOf(member.getId());
    }

    private void setSessionMemberInfo(Member member) {
//        String role = String.valueOf(AuthHelper.extractMemberRoles().stream().map(roleType -> roleType.equals(RoleType.ROLE_ADMIN)).findAny().orElse(null));
//        Object sample = AuthHelper.extractMemberRoles().stream().map(roleType -> roleType.equals(RoleType.ROLE_ADMIN)).findAny().orElse(null);
/*
        AuthHelper.extractMemberRoles().stream().forEach(roleType -> {
            log.info("ROLE TYPE FOREACH = {}", roleType);
            log.info("COMPARE RoleType.ROLE_ADMIN = {}", roleType == RoleType.ROLE_ADMIN);
        });
 */
        if(session != null) {
            session.setAttribute("username", member.getUsername());
        }

    }
}

