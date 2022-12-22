package hoon.community.domain.member.repository;

import hoon.community.domain.member.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member>findByEmail(String email);
    Optional<Member>findByUsername(String username);

    @EntityGraph("Member.roles")
    Optional<Member> findWithRolesById(Long id);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByLoginId(String loginId);
}
