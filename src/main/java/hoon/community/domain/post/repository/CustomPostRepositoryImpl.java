package hoon.community.domain.post.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hoon.community.domain.comment.entity.QComment;
import hoon.community.domain.post.dto.PostInfoDto;
import hoon.community.domain.post.dto.PostReadCondition;
import hoon.community.domain.post.entity.BoardType;
import hoon.community.domain.post.entity.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static hoon.community.domain.comment.entity.QComment.comment;
import static hoon.community.domain.post.entity.QPost.post;

@Transactional(readOnly = true)
@Slf4j
public class CustomPostRepositoryImpl extends QuerydslRepositorySupport implements CustomPostRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomPostRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Post.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<PostInfoDto> findAllByCondition(PostReadCondition condition) {
        Pageable pageable = PageRequest.of(condition.getPage(), condition.getSize());
        Predicate predicate = createPredicate(condition);
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }

    private List<PostInfoDto> fetchAll(Predicate predicate, Pageable pageable) {
        return getQuerydsl().applyPagination(
                pageable,
                jpaQueryFactory
                        .select(Projections.constructor(PostInfoDto.class, post.id, post.title, post.member.username, post.hits, post.comments.size(), post.existImages, post.createdDate))
                        .from(post)
                        .join(post.member)
                        .where(predicate)
                        .orderBy(post.id.desc())
        ).fetch();
    }

    private Long fetchCount(Predicate predicate) {
        return jpaQueryFactory.select(post.count()).from(post).where(predicate).fetchOne();
    }

    private Predicate createPredicate(PostReadCondition condition) {
        return new BooleanBuilder()
                .and(orConditionsByEqMemberIds(condition.getMemberId()))
                .and(orConditionsByEqUsername(condition.getUsername()))
                .and(orConditionsByContainsContent(condition.getContent()))
                .and(orConditionsByContainsTitle(condition.getTitle()))
                .and(orConditionsByBoardType(condition.getBoardType()));
    }

    private Predicate orConditionsByEqMemberIds(List<Long> memberIds) {
        return orConditions(memberIds, post.member.id::eq);
    }

    private Predicate orConditionsByEqUsername(List<String> username) {
        return orConditions(username, post.member.username::eq);
    }

    private Predicate orConditionsByContainsContent(List<String> content) {
        return orConditions(content, post.content::contains);
    }

    private Predicate orConditionsByContainsTitle(List<String> title) {
        return orConditions(title, post.title::contains);
    }

    private Predicate orConditionsByBoardType(List<BoardType> boardType) {
        return orConditions(boardType, post.boardType::eq);
    }


    private <T> Predicate orConditions(List<T> values, Function<T, BooleanExpression> term) {
        return values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }
}


