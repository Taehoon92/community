package hoon.community.global.security.oauth;

import hoon.community.domain.member.entity.Member;
import hoon.community.domain.member.repository.MemberRepository;
import hoon.community.domain.role.entity.RoleType;
import hoon.community.controller.exception.OAuthProviderMissMatchException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import springfox.documentation.service.OAuth;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {


        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        AuthProvider authProvider = AuthProvider.valueOf(registrationId.toUpperCase());
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(authProvider, oAuth2User.getAttributes());


        Optional<Member> foundMember = memberRepository.findByEmail(userInfo.getEmail());
        Member member;
        Set<RoleType> roleTypes = new HashSet<RoleType>();
        if (foundMember.isPresent()) {
            //가입된 경우
            member = foundMember.get();

            if (member.getAuthProvider() == null) { // 기존 회원 중 OAuth 첫 로그인
                member.updateModifiedDate();
                roleTypes = member.getRoleTypes();
                roleTypes.add(RoleType.ROLE_SOCIAL);
                member.updateRoles(roleTypes);
                member.updateAuthProvider(authProvider);
            } else if (member.getAuthProvider() != authProvider) { // OAuth로그인 Provider가 기존 Provider랑 다를때
//                throw new OAuthProviderMissMatchException("Wrong Provider! Try again with " + member.getAuthProvider());
                //Custom Exception 핸들링하는 방법 도저히 못찾음
                throw new OAuth2AuthenticationException("Wrong Provider! Try again with " + member.getAuthProvider());
            }

        } else { // 첫 로그인
            roleTypes.add(RoleType.ROLE_USER);
            roleTypes.add(RoleType.ROLE_SOCIAL);

            member = Member.builder()
                    .email(userInfo.getEmail())
                    .username(userInfo.getUsername())
                    .roleTypes(roleTypes)
                    .mainRole("USER")
                    .authProvider(authProvider)
                    .build();

            memberRepository.save(member);
        }

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                userNameAttributeName
        );

    }

}
