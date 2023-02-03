package hoon.community.domain.sign.service;

import hoon.community.domain.member.repository.MemberRepository;
import hoon.community.domain.role.entity.RoleType;
import hoon.community.domain.sign.dto.RefreshTokenResponse;
import hoon.community.domain.sign.dto.SignInRequest;
import hoon.community.domain.sign.dto.SignInResponse;
import hoon.community.domain.sign.dto.SignUpRequest;
import hoon.community.global.exception.CustomException;
import hoon.community.global.factory.entity.MemberFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SignServiceTest {

    SignService signService;
    @Mock MemberRepository memberRepository;
    @Mock RoleRepository roleRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock TokenHelper accessTokenHelper;
    @Mock TokenHelper refreshTokenHelper;

    @BeforeEach
    void beforeEach() {
        signService = new SignService(memberRepository, roleRepository, passwordEncoder, accessTokenHelper, refreshTokenHelper, null);
    }


    @Test
    void signUpTest() {
        //given
        SignUpRequest req = createSignUpRequest();
        given(roleRepository.findByRoleType(RoleType.ROLE_USER)).willReturn(Optional.of(new Role(RoleType.ROLE_USER)));

        //when
        signService.signUp(req);

        //then
        verify(passwordEncoder).encode(req.getPassword());
        verify(memberRepository).save(any());
    }

    @Test
    void validSignUpByDuplicateEmailTest() {
        //given
        given(memberRepository.existsByEmail(anyString())).willReturn(true);

        //when, then
//        assertThatThrownBy(() -> signService.signUp(createSignUpRequest())).isInstanceOf(MemberEmailAlreadyExistsException.class);
        assertThatThrownBy(() -> signService.signUp(createSignUpRequest())).isInstanceOf(CustomException.class);
    }

    @Test
    void signInTest() {
        //given
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(MemberFactory.createMember()));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
        given(accessTokenHelper.createToken(anyString())).willReturn("access");
        given(refreshTokenHelper.createToken(anyString())).willReturn("refresh");
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();

        //when
        SignInResponse response = signService.signIn(new SignInRequest("email", "password"), servletResponse);

        //then
        assertThat(response.getAccessToken()).isEqualTo("access");
        assertThat(response.getRefreshToken()).isEqualTo("refresh");
    }

    @Test
    void SignInExceptionByNoneMemberTest() {
        //given
        given(memberRepository.findByEmail(any())).willReturn(Optional.empty());
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();


        //when, then
        assertThatThrownBy(() -> signService.signIn(new SignInRequest("email", "password"), servletResponse)).isInstanceOf(CustomException.class);
    }

    @Test
    void signInExceptionByInvalidPasswordTest() {
        //given
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(MemberFactory.createMember()));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();

        //when, then
        assertThatThrownBy(() -> signService.signIn(new SignInRequest("email","password"), servletResponse)).isInstanceOf(CustomException.class);
    }

    @Test
    void refreshTokenTest() {
        //given
        String refreshToken = "refreshToken";
        String subject = "subject";
        String accessToken = "accessToken";
        given(refreshTokenHelper.validate(refreshToken)).willReturn(true);
        given(refreshTokenHelper.extractSubject(refreshToken)).willReturn(subject);
        given(accessTokenHelper.createToken(subject)).willReturn(accessToken);

        //when
        RefreshTokenResponse response = signService.refreshToken(refreshToken);

        //then
        assertThat(response.getAccessToken()).isEqualTo(accessToken);
    }

    @Test
    void refreshTokenExceptionByInvalidTokenTest() {
        //given
        String refreshToken = "refreshToken";
        given(refreshTokenHelper.validate(refreshToken)).willReturn(false);

        //when, then
        assertThatThrownBy(() -> signService.refreshToken(refreshToken))
                .isInstanceOf(CustomException.class);
    }



    private SignUpRequest createSignUpRequest() {
        return new SignUpRequest("password","username","email");
    }

}