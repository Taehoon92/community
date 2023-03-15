package hoon.community.domain.post.service;

import hoon.community.domain.member.repository.MemberRepository;
import hoon.community.domain.post.dto.*;
import hoon.community.domain.post.entity.BoardType;
import hoon.community.domain.post.entity.Image;
import hoon.community.domain.post.entity.Post;
import hoon.community.domain.post.repository.PostRepository;
import hoon.community.global.exception.CustomException;
import hoon.community.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final FileService fileService;


    @Transactional
    public PostCreateResponse create(PostCreateRequest request, BoardType boardType) {
        Post post = postRepository.save(PostCreateRequest.toEntity(request, memberRepository, boardType));
        uploadImages(post.getImages(), request.getImages());
        return new PostCreateResponse(post.getId());
    }

    public PostDto read(Long id) {
        Post entity = postRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        return PostDto.toDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        deleteImages(post.getImages());
        postRepository.delete(post);
    }

    @Transactional
    public PostCreateResponse update(Long id, PostUpdateRequest request) {
        Post post = postRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        post.decreaseHits();
        post.decreaseHits();
        ImageUpdatedResult result = post.update(request);
        uploadImages(result.getAddedImages(), result.getAddedImageFiles());
        deleteImages(result.getDeletedImages());
        return new PostCreateResponse(id);
    }

    public PostListDto readAll(PostReadCondition condition) {
        return PostListDto.toDto(
                postRepository.findAllByCondition(condition)
        );
    }

    @Transactional
    public int updateHits(Long id) {
        return postRepository.updateHits(id);
    }

    private void uploadImages(List<Image> images, List<MultipartFile> fileImages) {
        log.info("UPLOAD IMAGES METHOD");
        IntStream.range(0, images.size()).forEach(i -> fileService.upload(fileImages.get(i), images.get(i).getUniqueName()));
    }

    private void deleteImages(List<Image> images) {
        images.stream().forEach(i -> fileService.delete(i.getUniqueName()));
    }
}
