package hoon.community.domain.post.repository;

import hoon.community.domain.member.entity.Member;
import hoon.community.domain.member.repository.MemberRepository;
import hoon.community.domain.post.dto.PostUpdateRequest;
import hoon.community.domain.post.entity.Image;
import hoon.community.domain.post.entity.Post;
import hoon.community.global.exception.CustomException;
import hoon.community.global.exception.ErrorCode;
import hoon.community.global.factory.dto.PostUpdateRequestFactory;
import hoon.community.global.factory.entity.ImageFactory;
import hoon.community.global.factory.entity.MemberFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.stream.Collectors;

import static hoon.community.global.factory.entity.ImageFactory.createImage;
import static hoon.community.global.factory.entity.ImageFactory.createImageWithOriginName;
import static hoon.community.global.factory.entity.PostFactory.createPost;
import static hoon.community.global.factory.entity.PostFactory.createPostWithImages;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostRepositoryTest {
    @Autowired PostRepository postRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired ImageRepository imageRepository;
    @PersistenceContext EntityManager em;

    Member member;

    @BeforeEach
    void beforeEach() {
        member = memberRepository.save(MemberFactory.createMember());
    }

    //@Test
    void createAndReadTest() {
        //given
        Post post = postRepository.save(createPost());
        clear();

        //when
        Post foundPost = postRepository.findById(post.getId()).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        //then
        assertThat(foundPost.getId()).isEqualTo(post.getId());
        assertThat(foundPost.getTitle()).isEqualTo(post.getTitle());
    }

    //@Test
    void deleteTest() {
        //given
        Post post = postRepository.save(createPost());
        clear();

        //when
        postRepository.deleteById(post.getId());
        clear();

        //then
        assertThatThrownBy(() -> postRepository.findById(post.getId()).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND))).isInstanceOf(CustomException.class);
    }

    //@Test
    void createCascadeImageTest() {
        //given
        Post post = postRepository.save(createPostWithImages(member, List.of(createImage(), createImage())));
        clear();

        //when
        Post foundPost = postRepository.findById(post.getId()).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        //then
        List<Image> images = foundPost.getImages();
        assertThat(images.size()).isEqualTo(2);
    }

    //@Test
    void deleteCascadeImageTest() {
        //given
        Post post = postRepository.save(createPostWithImages(member, List.of(createImage(), createImage())));
        clear();

        //when
        postRepository.deleteById(post.getId());
        clear();

        //then
        List<Image> images = imageRepository.findAll();
        assertThat(images.size()).isZero();

    }

    //@Test
    void deleteCascadeByMemberTest() {
        //given
        postRepository.save(createPost(member));
        clear();

        //when
        memberRepository.deleteById(member.getId());
        clear();

        //then
        List<Post> result = postRepository.findAll();
        assertThat(result.size()).isZero();

    }

    //@Test
    void findByIdWithMemberTest() {
        //given
        Post post = postRepository.save(createPost(member));

        //when
        Post foundPost = postRepository.findByIdWithMember(post.getId()).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        //then
        Member foundMember = foundPost.getMember();
        assertThat(foundMember.getEmail()).isEqualTo(member.getEmail());
    }

    //@Test
    void updateTest() {
        //given
        Image a = createImageWithOriginName("a.jpg");
        Image b = createImageWithOriginName("b.jpg");
        Post post = postRepository.save(createPostWithImages(member, List.of(a,b)));
        clear();

        //when
        MockMultipartFile cFile = new MockMultipartFile("c", "c.png", MediaType.IMAGE_PNG_VALUE, "cFile".getBytes());
        PostUpdateRequest request = PostUpdateRequestFactory.createPostUpdateRequest("update title", "update content", List.of(cFile), List.of(a.getId()));
        Post foundPost = postRepository.findById(post.getId()).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        foundPost.update(request);
        clear();

        //then
        Post result = postRepository.findById(post.getId()).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        assertThat(result.getTitle()).isEqualTo(request.getTitle());
        assertThat(result.getContent()).isEqualTo(request.getContent());
        List<Image> images = result.getImages();
        List<String> originNames = images.stream().map(i -> i.getOriginName()).collect(Collectors.toList());
        assertThat(images.size()).isEqualTo(2);
        assertThat(originNames).contains(b.getOriginName(), cFile.getOriginalFilename());
        List<Image> resultImages = imageRepository.findAll();
        assertThat(resultImages.size()).isEqualTo(2);
    }

    void clear() {
        em.flush();
        em.clear();
    }
}