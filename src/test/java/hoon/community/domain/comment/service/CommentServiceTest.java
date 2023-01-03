package hoon.community.domain.comment.service;

import hoon.community.domain.comment.dto.CommentDto;
import hoon.community.domain.comment.dto.CommentReadCondition;
import hoon.community.domain.comment.repository.CommentRepository;
import hoon.community.domain.member.repository.MemberRepository;
import hoon.community.domain.post.repository.PostRepository;
import hoon.community.global.exception.CustomException;
import hoon.community.global.factory.dto.CommentCreateRequestFactory;
import hoon.community.global.factory.dto.CommentFactory;
import hoon.community.global.factory.dto.CommentReadConditionFactory;
import hoon.community.global.factory.entity.MemberFactory;
import hoon.community.global.factory.entity.PostFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static hoon.community.global.factory.dto.CommentCreateRequestFactory.createCommentCreateRequest;
import static hoon.community.global.factory.dto.CommentCreateRequestFactory.createCommentCreateRequestWithParentId;
import static hoon.community.global.factory.dto.CommentFactory.createComment;
import static hoon.community.global.factory.dto.CommentFactory.createDeletedComment;
import static hoon.community.global.factory.dto.CommentReadConditionFactory.createCommentReadCondition;
import static hoon.community.global.factory.entity.MemberFactory.createMember;
import static hoon.community.global.factory.entity.PostFactory.createPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @InjectMocks CommentService commentService;
    @Mock
    CommentRepository commentRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    PostRepository postRepository;

    @Test
    void readAllTest() {
        //given
        given(commentRepository.findAllWithMemberAndParentByPostIdOrderByParentIdAscNullsFirstCommentIdAsc(anyLong()))
                .willReturn(
                        List.of(createComment(null), createComment(null))
                );

        //when
        List<CommentDto> result = commentService.readAll(createCommentReadCondition());

        //then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void readAllDeletedCommentTest() {
        //given
        given(commentRepository.findAllWithMemberAndParentByPostIdOrderByParentIdAscNullsFirstCommentIdAsc(anyLong()))
                .willReturn(
                        List.of(createDeletedComment(null), createDeletedComment(null))
                );

        //when
        List<CommentDto> result = commentService.readAll(createCommentReadCondition());

        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getContent()).isNull();
        assertThat(result.get(0).getMember()).isNull();
    }

    @Test
    void createTest() {
        //given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
        given(postRepository.findById(anyLong())).willReturn(Optional.of(createPost()));

        //when
        commentService.create(createCommentCreateRequest());

        //then
        verify(commentRepository).save(any());
    }

    @Test
    void createExceptionByMemberNotFoundTest() {
        //given
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> commentService.create(createCommentCreateRequest()))
                .isInstanceOf(CustomException.class);
    }

    @Test
    void createExceptionByPostNotFoundTest() {
        //given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
        given(postRepository.findById(anyLong())).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> commentService.create(createCommentCreateRequest()))
                .isInstanceOf(CustomException.class);
    }

    @Test
    void createExceptionByCommentNotFoundTest() {
        //given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
        given(postRepository.findById(anyLong())).willReturn(Optional.of(createPost()));
        given(commentRepository.findById(anyLong())).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> commentService.create(createCommentCreateRequestWithParentId(1L)))
                .isInstanceOf(CustomException.class);
    }
}