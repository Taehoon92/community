package hoon.community.domain.sign.service;

import hoon.community.domain.member.entity.*;
import hoon.community.domain.role.entity.Role;
import hoon.community.domain.role.entity.RoleRepository;
import hoon.community.domain.role.entity.RoleType;
import hoon.community.domain.sign.dto.SignInRequest;
import hoon.community.domain.sign.dto.SignInResponse;
import hoon.community.domain.sign.dto.SignUpRequest;
import hoon.community.global.exception.CustomException;
import hoon.community.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Transactional
    public void signUp(SignUpRequest req) {
        validateSignUpInfo(req);
        String encodedPassword = passwordEncoder.encode(req.getPassword());
        List<Role> roles = List.of(roleRepository.findByRoleType(RoleType.ROLE_USER).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND)));
        memberRepository.save(SignUpRequest.toEntity(req, roles, passwordEncoder));
    }

    public SignInResponse signIn(SignInRequest req) {
        Member member = memberRepository.findByEmail(req.getEmail()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        validatePassword(req, member);
        String subject = createSubject(member);
        String accessToken = tokenService.createAccessToken(subject);
        String refreshToken = tokenService.createRefreshToken(subject);
        return new SignInResponse(accessToken, refreshToken);
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

    private String createSubject(Member member) {
        return String.valueOf(member.getId());
    }
}

