package hoon.community.domain.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hoon.community.domain.member.entity.Member;
import hoon.community.domain.member.repository.MemberRepository;
import hoon.community.domain.post.dto.PostInfoDto;
import hoon.community.domain.post.dto.PostReadCondition;
import hoon.community.domain.post.entity.Post;
import hoon.community.global.config.QuerydslConfig;
import hoon.community.global.factory.dto.PostReadConditionFactory;
import hoon.community.global.factory.entity.MemberFactory;
import hoon.community.global.factory.entity.PostFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomPostRepositoryImplTest {

    @Autowired PostRepository postRepository;
    @Autowired MemberRepository memberRepository;
    @PersistenceContext EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void init() {
        queryFactory = new JPAQueryFactory(em);
    }

//    @Test
    void findAllByConditionTest() {
        //given
        List<Member> members = saveMember(5);

        List<Post> posts = IntStream.range(0, 10)
                .mapToObj(i -> postRepository.save(PostFactory.createPost(members.get(i % 3)))).collect(Collectors.toList());
        clear();

        List<Long> memberIds = List.of(members.get(0).getId(), members.get(2).getId());
        int sizePerPage = 2;
        long expectedTotalElements = 7;

        PostReadCondition page0Condition = PostReadConditionFactory.createPostReadCondition(0, sizePerPage, memberIds);
        PostReadCondition page1Condition = PostReadConditionFactory.createPostReadCondition(1, sizePerPage, memberIds);

        //when
        Page<PostInfoDto> page0 = postRepository.findAllByCondition(page0Condition);
        Page<PostInfoDto> page1 = postRepository.findAllByCondition(page1Condition);

        //then
        assertThat(page0.getTotalElements()).isEqualTo(expectedTotalElements);
        assertThat(page0.getTotalPages()).isEqualTo((expectedTotalElements + 1) / sizePerPage);

        assertThat(page0.getContent().size()).isEqualTo(2);
        assertThat(page1.getContent().size()).isEqualTo(2);
    }



    private List<Member> saveMember(int size) {
        return IntStream.range(0, size)
                .mapToObj(i -> memberRepository.save(MemberFactory.createMember("password"+i, "username"+i, "member"+i+"@email.com"))).collect(Collectors.toList());
    }
    private void clear() {
        em.flush();
        em.clear();
    }
}