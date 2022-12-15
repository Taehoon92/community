package hoon.community;

import hoon.community.domain.member.entity.Member;
import hoon.community.domain.member.entity.MemberRepository;
import hoon.community.domain.role.entity.Role;
import hoon.community.domain.role.entity.RoleRepository;
import hoon.community.domain.role.entity.RoleType;
import hoon.community.global.exception.CustomException;
import hoon.community.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

//@Component
@RequiredArgsConstructor
@Slf4j
public class InitDB {
    private final RoleRepository roleRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

//    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initDB() {
        log.info("initialize database");

        initRole();
        initMemberAdmin();
        initMemberTester();

        log.info("initialized database");

    }

    private void initRole() {
        roleRepository.saveAll(
                List.of(RoleType.values()).stream().map(roleType -> new Role(roleType)).collect(Collectors.toSet())
        );
    }

    private void initMemberAdmin() {
        memberRepository.save(
                new Member("admin", passwordEncoder.encode("password1!"), "admin", "admin@admin.com",
                        List.of(roleRepository.findByRoleType(RoleType.ROLE_USER).orElseThrow(() -> new CustomException(ErrorCode.ROLE_NOT_FOUND)),
                                roleRepository.findByRoleType(RoleType.ROLE_ADMIN).orElseThrow(() -> new CustomException(ErrorCode.ROLE_NOT_FOUND)))
                )
        );
    }

    private void initMemberTester() {
        memberRepository.saveAll(
                List.of(
                        new Member("tester1", passwordEncoder.encode("password1!"), "tester1", "tester1@tester.com",
                                List.of(roleRepository.findByRoleType(RoleType.ROLE_USER).orElseThrow(()-> new CustomException(ErrorCode.ROLE_NOT_FOUND)))),
                        new Member("tester2", passwordEncoder.encode("password1!"), "tester2", "tester2@tester.com",
                                List.of(roleRepository.findByRoleType(RoleType.ROLE_USER).orElseThrow(()-> new CustomException(ErrorCode.ROLE_NOT_FOUND))))
                )
        );
    }
}
