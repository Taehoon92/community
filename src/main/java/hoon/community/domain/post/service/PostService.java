package hoon.community.domain.post.service;

import hoon.community.domain.member.repository.MemberRepository;
import hoon.community.domain.post.dto.*;
import hoon.community.domain.post.entity.Post;
import hoon.community.domain.post.repository.PostRepository;
import hoon.community.global.exception.CustomException;
import hoon.community.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public PostCreateResponse create(PostCreateRequest request) {
        Post post = postRepository.save(PostCreateRequest.toEntity(request, memberRepository));
        return new PostCreateResponse(post.getId());
    }

    public PostDto read(Long id) {
        return PostDto.toDto(postRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND)));
    }

    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        postRepository.delete(post);
    }

    @Transactional
    public void update(Long id, PostUpdateRequest request) {
        Post post = postRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        post.update(request);
    }

    public PostListDto readAll(PostReadCondition condition) {
        return PostListDto.toDto(
                postRepository.findAllByCondition(condition)
        );
    }
}
