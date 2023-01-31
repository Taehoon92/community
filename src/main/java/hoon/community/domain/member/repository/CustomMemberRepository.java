package hoon.community.domain.member.repository;

import hoon.community.domain.member.dto.MemberDetailsDto;
import hoon.community.domain.member.dto.MemberDetailsTestDto;
import hoon.community.domain.member.dto.MemberDetailsTestTestDto;
import hoon.community.domain.member.dto.MemberReadCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;

public interface CustomMemberRepository {
    Page<MemberDetailsTestDto> findAllByCondition(MemberReadCondition condition);

    Page<MemberDetailsTestTestDto> findAllByCondition2(MemberReadCondition condition);

}
