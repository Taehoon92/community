package hoon.community.global.security;

import hoon.community.domain.member.entity.Member;
import hoon.community.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseGet(() -> new Member(null, null, null, List.of()));
        return new CustomUserDetails(
                String.valueOf(member.getId()),
                String.valueOf(member.getEmail()),
                member.getRoleTypes().stream().map(roleType -> roleType.toString())
                                .map(SimpleGrantedAuthority::new).collect(Collectors.toSet())
        );
    }
}

