package hoon.community.domain.member.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member>findByEmail(String email);
    Optional<Member>findByUsername(String username);
    Optional<Member> findByLoginId(String loginId);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByLoginId(String loginId);
}
