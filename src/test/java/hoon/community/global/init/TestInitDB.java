package hoon.community.global.init;

import hoon.community.domain.member.entity.Member;
import hoon.community.domain.member.repository.MemberRepository;
import hoon.community.domain.role.entity.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TestInitDB {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    private String adminEmail = "admin@admin.com";
    private String member1Email = "member1@member.com";
    private String member2Email = "member2@member.com";
    private String password = "123456a!";

    @Transactional
    public void initDB() {
        initRole();
        initTestAdmin();
        initTestMember();
    }

    private void initRole() {
        roleRepository.saveAll(
                List.of(RoleType.values()).stream().map(roleType -> new Role(roleType)).collect(Collectors.toList())
        );
    }

    private void initTestAdmin() {
        memberRepository.save(
                new Member(passwordEncoder.encode(password), "admin", adminEmail, List.of(RoleType.ROLE_ADMIN, RoleType.ROLE_USER)
                ));
    }

    private void initTestMember() {
        memberRepository.saveAll(
                List.of(
                        new Member(passwordEncoder.encode(password), "member1", member1Email,
                                List.of(RoleType.ROLE_USER)),
                        new Member(passwordEncoder.encode(password), "member2", member2Email,
                                List.of(RoleType.ROLE_USER)))
        );
    }


    public String getAdminEmail() {
        return adminEmail;
    }

    public String getMember1Email() {
        return member1Email;
    }

    public String getMember2Email() {
        return member2Email;
    }

    public String getPassword() {
        return password;
    }
}
