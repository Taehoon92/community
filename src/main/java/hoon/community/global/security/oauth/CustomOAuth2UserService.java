package hoon.community.global.security.oauth;

import hoon.community.domain.member.entity.Member;
import hoon.community.domain.member.repository.MemberRepository;
import hoon.community.domain.member.service.MemberService;
import hoon.community.domain.role.entity.RoleType;
import hoon.community.global.security.CustomUserDetails;
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

import javax.management.relation.Role;
import java.util.*;
import java.util.stream.Collectors;

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
        if(foundMember.isPresent()) {
            //가입된 경우
            member = foundMember.get();

            if(member.getAuthProvider() == null) { // 기존 회원 중 OAuth 첫 로그인
                log.info("기존회원 첫 OAuth 로그인");
                member.updateModifiedDate();
                roleTypes = member.getRoleTypes();
                roleTypes.add(RoleType.ROLE_SOCIAL);
                member.updateRoles(roleTypes);
                member.updateAuthProvider(authProvider);
            } else if(member.getAuthProvider() != authProvider){ // OAuth로그인 Provider가 기존 Provider랑 다를때
                log.info("다른 Auth Provider로 접근");
            } else {
                log.info("기존회원 로그인");
            }


        } else {
            log.info("USER INFO = {}", userInfo);

            roleTypes.add(RoleType.ROLE_USER);
            roleTypes.add(RoleType.ROLE_SOCIAL);

            if(authProvider == AuthProvider.GITHUB) {
                log.info("AUTH PROVIDER = {}", authProvider);
                member = Member.builder()
                        .email(userInfo.getId())
                        .username(userInfo.getUsername())
                        .roleTypes(roleTypes)
                        .mainRole("USER")
                        .authProvider(authProvider)
                        .build();

            }

            else {
                log.info("AUTH PROVIDER = {}", authProvider);
                member = Member.builder()
                        .email(userInfo.getEmail())
                        .username(userInfo.getUsername())
                        .roleTypes(roleTypes)
                        .mainRole("USER")
                        .authProvider(authProvider)
                        .build();
            }

            memberRepository.save(member);


        }






        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                userNameAttributeName
        );


//        OAuthAttributes oAuthAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
//
//        log.info("Custom OAuth2User Service - OAuthAttributes : = {}",oAuthAttributes);


        /*
        Assert.notNull(userRequest, "userRequest cannot be null");
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        ClientRegistration.ProviderDetails.UserInfoEndpoint userInfoEndpoint = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint();

        String userInfoUri = userInfoEndpoint.getUri();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userInfoEndpoint.getUserNameAttributeName();
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        Member member = saveOrUpdate(attributes);


        Map<String, Object> attributes = getAttributes(userRequest);

        return process()

         */
    }

/*
    private Member createMember(OAuth2UserInfo userInfo, AuthProvider authProvider) {
        Set<RoleType> roleTypes = new HashSet<>();
        roleTypes.add(RoleType.ROLE_SOCIAL);
        roleTypes.add(RoleType.ROLE_USER);

        String mainRole = Member.setMainRole(roleTypes);


        Member member = Member.builder()
                .email(userInfo.getEmail())
                .roleTypes(roleTypes)
                .mainRole(mainRole)
                .build();
    }


 */

/*
    @Transactional
    public void saveOrUpdate(OAuth2UserInfo userInfo) {

        log.info("USER INFO = {}", userInfo);
        log.info("USER INFO - SUB = {}",userInfo.getId());
        log.info("USER INFO - NAME = {}",userInfo.getUsername());
        log.info("USER INFO - EMAIL = {}",userInfo.getEmail());

        Optional<Member> foundMember = memberRepository.findByEmail(userInfo.getEmail());
        Member member;
        Set<RoleType> roleTypes = new HashSet<RoleType>();
        log.info("Found Member = {}", foundMember);
        if(foundMember.isPresent()) {
            //가입된 경우
            member = foundMember.get();
            member.updateModifiedDate();
            roleTypes = member.getRoleTypes();
            log.info("ROLE TYPES  = {}", roleTypes);
//            roleTypes.add(RoleType.ROLE_SOCIAL);
//            member.updateRoles(roleTypes);

            log.info("이미 가입");

        } else {
            //새로 가입하는 경우
            log.info("새로 가입");
        }

    }
*/
}
