package hoon.community.domain.post.service;

import hoon.community.domain.member.repository.MemberRepository;
import hoon.community.domain.post.dto.PostCreateRequest;
import hoon.community.domain.post.dto.PostDto;
import hoon.community.domain.post.dto.PostUpdateRequest;
import hoon.community.domain.post.entity.Post;
import hoon.community.domain.post.repository.PostRepository;
import hoon.community.global.exception.CustomException;
import hoon.community.global.exception.ErrorCode;
import hoon.community.global.factory.dto.PostCreateRequestFactory;
import hoon.community.global.factory.dto.PostUpdateRequestFactory;
import hoon.community.global.factory.entity.MemberFactory;
import hoon.community.global.factory.entity.PostFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static hoon.community.global.factory.dto.PostCreateRequestFactory.createPostCreateRequest;
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
class PostServiceTest {

    @InjectMocks PostService postService;
    @Mock PostRepository postRepository;
    @Mock MemberRepository memberRepository;

    @Test
    void createTest() {
        //given
        PostCreateRequest request = createPostCreateRequest();
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
        given(postRepository.save(any())).willReturn(createPost());

        //when
        postService.create(request);

        //then
        verify(postRepository).save(any());
    }

    @Test
    void createExceptionByMemberNotFoundTest() {
        //given
        PostCreateRequest request = createPostCreateRequest();
        given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));

        //when, then
        assertThatThrownBy(() -> postService.create(request)).isInstanceOf(CustomException.class);
    }

    @Test
    void readTest() {
        //given
        Post post = createPost();
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        //when
        PostDto postDto = postService.read(1L);

        //then
        assertThat(postDto.getTitle()).isEqualTo(post.getTitle());
    }

    @Test
    void readExceptionByPostNotFoundTest() {
        //given
        given(postRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));

        //when, then
        assertThatThrownBy(() -> postService.read(1L)).isInstanceOf(CustomException.class);
    }

    @Test
    void deleteTest() {
        //given
        Post post = createPost();
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        //when
        postService.delete(1L);

        //then
        verify(postRepository).delete(any());
    }

    @Test
    void deleteExceptionByNotFoundPostTest() {
        //given
        given(postRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));

        //when, then
        assertThatThrownBy(() -> postService.delete(1L)).isInstanceOf(CustomException.class);
    }

    @Test
    void updateTest() {
        //given
        Post post = createPost();
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        PostUpdateRequest postUpdateRequest = PostUpdateRequestFactory.createPostUpdateRequest("update title", "update content");

        //when
        postService.update(1L, postUpdateRequest);
        Post updatedPost = postRepository.findById(1L).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));

        //then
        assertThat(updatedPost.getTitle()).isEqualTo(postUpdateRequest.getTitle());
        assertThat(updatedPost.getContent()).isEqualTo(postUpdateRequest.getContent());
    }

    @Test
    void updateExceptionByPostNotFoundTest() {
        //given
        given(postRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));

        //when, then
        assertThatThrownBy(() -> postService.update(1L, PostUpdateRequestFactory.createPostUpdateRequest("title", "content"))).isInstanceOf(CustomException.class);
    }

}