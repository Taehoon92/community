package hoon.community.domain.post.service;

import hoon.community.domain.member.repository.MemberRepository;
import hoon.community.domain.post.dto.PostCreateRequest;
import hoon.community.domain.post.dto.PostDto;
import hoon.community.domain.post.dto.PostListDto;
import hoon.community.domain.post.dto.PostUpdateRequest;
import hoon.community.domain.post.entity.Image;
import hoon.community.domain.post.entity.Post;
import hoon.community.domain.post.repository.PostRepository;
import hoon.community.global.exception.CustomException;
import hoon.community.global.exception.ErrorCode;
import hoon.community.global.factory.dto.PostReadConditionFactory;
import hoon.community.global.factory.dto.PostUpdateRequestFactory;
import hoon.community.global.factory.entity.ImageFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static hoon.community.global.factory.dto.PostCreateRequestFactory.createPostCreateRequest;
import static hoon.community.global.factory.dto.PostCreateRequestFactory.createPostCreateRequestWithImages;
import static hoon.community.global.factory.dto.PostUpdateRequestFactory.createPostUpdateRequest;
import static hoon.community.global.factory.entity.ImageFactory.*;
import static hoon.community.global.factory.entity.MemberFactory.createMember;
import static hoon.community.global.factory.entity.PostFactory.createPost;
import static hoon.community.global.factory.entity.PostFactory.createPostWithImages;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks PostService postService;
    @Mock PostRepository postRepository;
    @Mock MemberRepository memberRepository;
    @Mock FileService fileService;

    @Test
    void createTest() {
        //given
        PostCreateRequest request = createPostCreateRequest();
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
//        given(postRepository.save(any())).willReturn(createPost());
        given(postRepository.save(any())).willReturn(createPostWithImages(
                IntStream.range(0, request.getImages().size()).mapToObj(i -> createImage()).collect(Collectors.toList())
        ));

        //when
        postService.create(request);

        //then
        verify(postRepository).save(any());
        verify(fileService, times(request.getImages().size())).upload(any(), anyString());
    }

    @Test
    void createExceptionByMemberNotFoundTest() {
        //given
        PostCreateRequest request = createPostCreateRequest();
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> postService.create(request)).isInstanceOf(CustomException.class);
    }

    @Test
    void createExceptionByUnsupportedImageFormatExceptionTest() {
        //given
        PostCreateRequest request = createPostCreateRequestWithImages(
                List.of(new MockMultipartFile("test", "test.txt", MediaType.TEXT_PLAIN_VALUE, "test".getBytes()))
        );
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));

        //when, then
        assertThatThrownBy(() -> postService.create(request)).isInstanceOf(CustomException.class);
    }

    @Test
    void readTest() {
        //given
        Post post = createPostWithImages(List.of(createImage(), createImage()));
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        //when
        PostDto postDto = postService.read(1L);

        //then
        assertThat(postDto.getTitle()).isEqualTo(post.getTitle());
        assertThat(postDto.getImages().size()).isEqualTo(post.getImages().size());
    }

    @Test
    void readExceptionByPostNotFoundTest() {
        //given
        given(postRepository.findById(anyLong())).willReturn(Optional.empty());

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
        given(postRepository.findById(anyLong())).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> postService.delete(1L)).isInstanceOf(CustomException.class);
    }

    @Test
    void updateTest() {
        //given
        Image a = createImageWithIdAndOriginName(1L, "a.png");
        Image b = createImageWithIdAndOriginName(2L, "b.png");
        Post post = createPostWithImages(List.of(a, b));
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        MockMultipartFile cFile = new MockMultipartFile("c","c.png",MediaType.IMAGE_PNG_VALUE, "c".getBytes());
        PostUpdateRequest postUpdateRequest = createPostUpdateRequest("update title", "update content", List.of(cFile), List.of(a.getId()));

        //when
        postService.update(1L, postUpdateRequest);

        //then
        List<Image> images = post.getImages();
        List<String> originNames = images.stream().map(i -> i.getOriginName()).collect(Collectors.toList());

        assertThat(originNames.size()).isEqualTo(2);
        assertThat(originNames).contains(b.getOriginName(), cFile.getOriginalFilename());

        verify(fileService, times(1)).upload(any(), anyString());
        verify(fileService, times(1)).delete(anyString());

        assertThat(post.getTitle()).isEqualTo(postUpdateRequest.getTitle());
        assertThat(post.getContent()).isEqualTo(postUpdateRequest.getContent());
    }

    @Test
    void updateExceptionByPostNotFoundTest() {
        //given
        given(postRepository.findById(anyLong())).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> postService.update(1L, createPostUpdateRequest("title", "content", List.of(), List.of()))).isInstanceOf(CustomException.class);
    }

    @Test
    void readAllTest() {
        //given
        given(postRepository.findAllByCondition(any())).willReturn(Page.empty());

        //when
        PostListDto postListDto = postService.readAll(PostReadConditionFactory.createPostReadCondition(1, 1));

        //then
        assertThat(postListDto.getPostList().size()).isZero();
    }

}