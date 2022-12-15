package hoon.community.domain.sign.service;

import hoon.community.domain.member.entity.Member;
import hoon.community.domain.member.entity.MemberRepository;
import hoon.community.domain.sign.dto.SignInRequest;
import hoon.community.domain.sign.dto.SignInResponse;
import hoon.community.domain.sign.dto.SignUpRequest;
import hoon.community.global.exception.CustomException;
import hoon.community.global.factory.entity.MemberFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

    @InjectMocks SignService signService;
    @Mock MemberRepository memberRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock TokenService tokenService;

    @Test
    void signUpTest() {
        //given
        SignUpRequest req = createSignUpRequest();

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
        given(tokenService.createAccessToken(anyString())).willReturn("access");
        given(tokenService.createRefreshToken(anyString())).willReturn("refresh");

        //when
        SignInResponse response = signService.signIn(new SignInRequest("email", "password"));

        //then
        assertThat(response.getAccessToken()).isEqualTo("access");
        assertThat(response.getRefreshToken()).isEqualTo("refresh");
    }

    @Test
    void SignInExceptionByNoneMemberTest() {
        //given
        given(memberRepository.findByEmail(any())).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> signService.signIn(new SignInRequest("email", "password"))).isInstanceOf(CustomException.class);
    }

    @Test
    void signInExceptionByInvalidPasswordTest() {
        //given
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(MemberFactory.createMember()));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

        //when, then
        assertThatThrownBy(() -> signService.signIn(new SignInRequest("email","password"))).isInstanceOf(CustomException.class);
    }



    private SignUpRequest createSignUpRequest() {
        return new SignUpRequest("loginId","password","username","email");
    }

}