package hoon.community.domain.member.repository;

import hoon.community.domain.member.dto.MemberDetailsTestDto;
import hoon.community.domain.member.dto.MemberDetailsTestTestDto;
import hoon.community.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, CustomMemberRepository {

    Optional<Member>findByEmail(String email);
    Optional<Member>findByUsername(String username);

    @EntityGraph("Member.roles")
    Optional<Member> findWithRolesById(Long id);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

//    Page<MemberDetailsTestTestDto> findAllOrderByIdDesc(Pageable pageable);
}
