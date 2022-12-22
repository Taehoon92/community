package hoon.community.domain.post.repository;

import hoon.community.domain.member.entity.Member;
import hoon.community.domain.member.repository.MemberRepository;
import hoon.community.domain.post.entity.Post;
import hoon.community.global.exception.CustomException;
import hoon.community.global.exception.ErrorCode;
import hoon.community.global.factory.entity.MemberFactory;
import hoon.community.global.factory.entity.PostFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static hoon.community.global.factory.entity.PostFactory.createPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostRepositoryTest {
    @Autowired PostRepository postRepository;
    @Autowired MemberRepository memberRepository;
    @PersistenceContext EntityManager em;

    Member member;

    @BeforeEach
    void beforeEach() {
        member = memberRepository.save(MemberFactory.createMember());
    }

    @Test
    void createAndReadTest() {
        //given
        Post post = postRepository.save(createPost());
        clear();

        //when
        Post foundPost = postRepository.findById(post.getId()).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));

        //then
        assertThat(foundPost.getId()).isEqualTo(post.getId());
        assertThat(foundPost.getTitle()).isEqualTo(post.getTitle());
    }

    @Test
    void deleteTest() {
        //given
        Post post = postRepository.save(createPost());
        clear();

        //when
        postRepository.deleteById(post.getId());
        clear();

        //then
        assertThatThrownBy(() -> postRepository.findById(post.getId()).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND))).isInstanceOf(CustomException.class);
    }

    @Test
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

    @Test
    void findByIdWithMemberTest() {
        //given
        Post post = postRepository.save(createPost(member));

        //when
        Post foundPost = postRepository.findByIdWithMember(post.getId()).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));

        //then
        Member foundMember = foundPost.getMember();
        assertThat(foundMember.getEmail()).isEqualTo(member.getEmail());
    }

    void clear() {
        em.flush();
        em.clear();
    }
}