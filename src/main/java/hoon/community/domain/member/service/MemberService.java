package hoon.community.domain.member.service;

import hoon.community.domain.member.dto.*;
import hoon.community.domain.member.entity.Member;
import hoon.community.domain.member.repository.MemberRepository;
import hoon.community.domain.role.dto.RoleModifyDto;
import hoon.community.domain.role.entity.RoleType;
import hoon.community.global.exception.CustomException;
import hoon.community.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public MemberDto read(Long id) {
        return MemberDto.toDto(memberRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)));
    }

    public MemberListDto readAll(MemberReadCondition condition) {
        return MemberListDto.toDto(memberRepository.findAllByCondition(condition));
    }

    @Transactional
    public void delete(Long id){
        if(notExistsMember(id)) throw new CustomException(ErrorCode.USER_NOT_FOUND);
        memberRepository.deleteById(id);
    }

    public MemberDetailsDto memberDetails(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return MemberDetailsDto.toDto(member);
    }

    private boolean notExistsMember(Long id) {
        return !memberRepository.existsById(id);
    }

    @Transactional
    public boolean modifyPassword(MemberPasswordModifyDto request, BindingResult bindingResult) {
        Member member = memberRepository.findById(request.getMemberId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        //현재 비밀번호가 일치하는지 확인
        if(!passwordEncoder.matches(request.getOldPassword(), member.getPassword())) {
            bindingResult.addError(new FieldError("dto", "oldPassword", "이전 비밀번호가 일치하지 않습니다."));
            return false;
        };

        //현재 비밀번호와 새 비밀번호가 다른지 확인
        if(request.getOldPassword().equals(request.getNewPassword())) {
            bindingResult.addError(new FieldError("dto", "newPassword", "변경할 비밀번호는 현재 비밀번호와 달라야합니다."));
            return false;
        }

        //새 비밀번호를 저장
        member.updatePassword(passwordEncoder.encode(request.getNewPassword()));
        return true;
    }


    @Transactional
    public boolean modifyRoles(Long memberId, RoleModifyDto dto) {

        Member member = memberRepository.findById(memberId).orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        if(dto.getRoles() != null) {
            Set<RoleType> roleTypes = dto.getRoles().stream().map(str -> {
                if(str.equals("ADMIN")) {
                    return RoleType.ROLE_ADMIN;
                } else if(str.equals("USER")) {
                    return RoleType.ROLE_USER;
                } else if(str.equals("SOCIAL")) {
                    return RoleType.ROLE_SOCIAL;
                } else {
                    return null;
                }
            }).collect(Collectors.toSet());

            member.updateRoles(roleTypes);
        }
        else {
            return false;
        }

        return true;
    }
}
