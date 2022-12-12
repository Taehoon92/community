package hoon.community.domain.member.service;

import hoon.community.domain.member.dto.MemberDto;
import hoon.community.domain.member.entity.MemberRepository;
import hoon.community.global.exception.CustomException;
import hoon.community.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberDto read(Long id) {
        return MemberDto.toEntity(memberRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)));
    }

    @Transactional
    public void delete(Long id){
        if(notExistsMember(id)) throw new CustomException(ErrorCode.USER_NOT_FOUND);
        memberRepository.deleteById(id);
    }

    private boolean notExistsMember(Long id) {
        return !memberRepository.existsById(id);
    }
}
