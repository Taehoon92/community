package hoon.community.domain.member.service;

import hoon.community.domain.member.dto.MemberDto;
import hoon.community.domain.member.entity.Member;
import hoon.community.domain.member.repository.MemberRepository;
import hoon.community.global.exception.CustomException;
import hoon.community.global.factory.entity.MemberFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks MemberService memberService;
    @Mock MemberRepository memberRepository;

    @Test
    void readTest() {
        //given
        Member member = MemberFactory.createMember();
        BDDMockito.given(memberRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.of(member));

        //when
        MemberDto result = memberService.read(1L);

        //then
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    void readExceptionByMemberNotFoundTest() {
        //given
        BDDMockito.given(memberRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.ofNullable(null));

        //when, then
        assertThatThrownBy(() -> memberService.read(1L)).isInstanceOf(CustomException.class);
    }

    @Test
    void deleteTest() {
        //given
        BDDMockito.given(memberRepository.existsById(ArgumentMatchers.anyLong())).willReturn(true);

        //when
        memberService.delete(1L);

        //then
        BDDMockito.verify(memberRepository).deleteById(ArgumentMatchers.anyLong());
    }

    @Test
    void deleteExceptionByMemberNotFoundTest() {
        //given
        BDDMockito.given(memberRepository.existsById(ArgumentMatchers.anyLong())).willReturn(false);

        //when, then
        assertThatThrownBy(() -> memberService.delete(1L)).isInstanceOf(CustomException.class);
    }


}