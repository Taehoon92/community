package hoon.community.domain.member.repository;

import hoon.community.domain.member.dto.MemberDetailsDto;
import hoon.community.domain.member.dto.MemberReadCondition;
import org.springframework.data.domain.Page;

public interface CustomMemberRepository {
    Page<MemberDetailsDto> findAllByCondition(MemberReadCondition condition);

}
