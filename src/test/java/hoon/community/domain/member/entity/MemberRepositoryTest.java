package hoon.community.domain.member.entity;

import hoon.community.domain.member.repository.MemberRepository;
import hoon.community.domain.role.entity.Role;
import hoon.community.domain.role.repository.RoleRepository;
import hoon.community.domain.role.entity.RoleType;
import hoon.community.global.exception.CustomException;
import hoon.community.global.exception.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static hoon.community.global.factory.entity.MemberFactory.createMember;
import static hoon.community.global.factory.entity.MemberFactory.createMemberWithRoles;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RoleRepository roleRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    void createAndReadTest() {
        //given
        Member member = createMember();

        //when
        memberRepository.save(member);
        clear();

        //then
        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        assertThat(foundMember.getId()).isEqualTo(member.getId());
    }

    @Test
    void memberDateTest() {
        //given
        Member member = createMember();

        //when
        memberRepository.save(member);
        clear();

        //then
        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        assertThat(foundMember.getCreatedDate()).isNotNull();
        assertThat(foundMember.getModifiedDate()).isNotNull();
        assertThat(foundMember.getCreatedDate()).isEqualTo(foundMember.getModifiedDate());
    }

    @Test
    void memberUpdateTest() {
        //given
        Member member = memberRepository.save(createMember());
        clear();

        //when
        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        foundMember.updateUsername("new Username");
        foundMember.updatePassword("newPassword");
        clear();

        //then
        Member updatedMember = memberRepository.findById(member.getId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        assertThat(updatedMember.getUsername()).isEqualTo("new Username");
        assertThat(updatedMember.getPassword()).isEqualTo("newPassword");
        assertThat(updatedMember.getUsername()).isNotEqualTo(member.getUsername());
    }

    @Test
    void memberDeleteTest() {
        //given
        Member member = memberRepository.save(createMember());
        clear();

        //when
        memberRepository.delete(member);
        clear();

        //then
        assertThatThrownBy(() -> memberRepository.findById(member.getId()).orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND))).isInstanceOf(CustomException.class);
    }

    @Test
    void memberFindByEmailTest() {
        //given
        Member member = memberRepository.save(createMember());
        clear();

        //when
        Member foundMember = memberRepository.findByEmail(member.getEmail()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        //then
        assertThat(foundMember.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    void memberFindByUsernameTest() {
        //given
        Member member = memberRepository.save(createMember());
        clear();

        //when
        Member foundMember = memberRepository.findByUsername(member.getUsername()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        //then
        assertThat(foundMember.getUsername()).isEqualTo(member.getUsername());
    }

    @Test
    void memberUniqueEmailTest() {
        //given
        Member member = memberRepository.save(createMember("loginId1","password1","username1","email1"));
        clear();

        //when, then
        assertThatThrownBy(() -> memberRepository.save(createMember("loginId2", "password2", "username2", member.getEmail())))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void memberUniqueloginIdTest() {
        //given
        Member member = memberRepository.save(createMember("loginId1", "password1", "username1", "email1"));
        clear();

        //when, then
        assertThatThrownBy(() -> memberRepository.save(createMember(member.getLoginId(), "password2", "username2", "email2")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void memberUniqueUsernameTest() {
        //given
        Member member = memberRepository.save(createMember("loginId1", "password1", "username1", "email"));
        clear();

        //when, then
        assertThatThrownBy(() -> memberRepository.save(createMember("loginId2", "password2", "username1", "email2")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void memberExistsByEmailTest() {
        //given
        Member member = memberRepository.save(createMember());
        clear();

        //when,then
        assertThat(memberRepository.existsByEmail(member.getEmail())).isTrue();
        assertThat(memberRepository.existsByEmail(member.getEmail()+"a")).isFalse();
    }

    @Test
    void memberExistsByLoginIdTest() {
        //given
        Member member = memberRepository.save(createMember());
        clear();

        //when,then
        assertThat(memberRepository.existsByLoginId(member.getLoginId())).isTrue();
        assertThat(memberRepository.existsByLoginId(member.getLoginId()+"a")).isFalse();
    }

    @Test
    void memberExistsByUsernameTest() {
        //given
        Member member = memberRepository.save(createMember());
        clear();

        //when,then
        assertThat(memberRepository.existsByUsername(member.getUsername())).isTrue();
        assertThat(memberRepository.existsByUsername(member.getUsername()+"a")).isFalse();
    }

    @Test
    void memberRoleCascadePersistTest() {
        //given
        List<RoleType> roleTypes = List.of(RoleType.ROLE_USER, RoleType.ROLE_SOCIAL, RoleType.ROLE_ADMIN);
        List<Role> roles = roleTypes.stream().map(roleType -> new Role(roleType)).collect(Collectors.toList());
        roleRepository.saveAll(roles);
        clear();

        Member member = memberRepository.save(createMemberWithRoles(roleRepository.findAll()));
        clear();

        //when
        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Set<MemberRole> memberRoles = foundMember.getRoles();

        //then
        assertThat(memberRoles.size()).isEqualTo(roles.size());
    }

    private void clear() {
        em.flush();
        em.clear();
    }

//    @AfterEach
    void teardown() {
        this.memberRepository.deleteAll();
        this.em.createNativeQuery("alter table `member` auto_increment = 1")
                .executeUpdate();
    }
}