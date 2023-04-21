package hoon.community.domain.member.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hoon.community.domain.member.dto.MemberDetailsDto;
import hoon.community.domain.member.dto.MemberReadCondition;
import hoon.community.domain.member.entity.Member;
import hoon.community.domain.role.entity.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static hoon.community.domain.member.entity.QMember.member;

@Transactional(readOnly = true)
@Slf4j
public class CustomMemberRepositoryImpl extends QuerydslRepositorySupport implements CustomMemberRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomMemberRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Member.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<MemberDetailsDto> findAllByCondition(MemberReadCondition condition) {
        Pageable pageable = PageRequest.of(condition.getPage(), condition.getSize());
        Predicate predicate = createPredicate(condition);
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }

    private List<MemberDetailsDto> fetchAll(Predicate predicate, Pageable pageable) {
        List<Member> memberList = getQuerydsl().applyPagination(
                pageable,
                jpaQueryFactory
                        .selectFrom(member)
//                        .leftJoin(member.roles).fetchJoin()
//                        .join(member.roles)
                        .where(predicate)
                        .orderBy(member.id.desc())
        ).fetch();

        return memberList.stream().map(member -> MemberDetailsDto.toDto(member)).collect(Collectors.toList());
    }



    private Long fetchCount(Predicate predicate) {
        return jpaQueryFactory.select(member.count()).from(member).where(predicate).fetchOne();
    }

    private Predicate createPredicate(MemberReadCondition condition) {

        //Role 검색 관련
        List<RoleType> roleTypeList = new ArrayList<>();

        if(condition.getRole() != null ) {
            List<String> rolesString = condition.getRole();
            String role = String.join("",rolesString);
            log.info("ROLE 추출값 ={}=",role);
            if(role.equals("ADMIN")) {
                roleTypeList = List.of(RoleType.ROLE_ADMIN);
            } else if(role.equals("USER")) {
                roleTypeList = List.of(RoleType.ROLE_USER);
            } else if(role.equals("SOCIAL")) {
                roleTypeList = List.of(RoleType.ROLE_SOCIAL);
            }
        }

        return new BooleanBuilder()
                .and(orConditionByEqMemberId(condition.getMemberId()))
                .and(orConditionByEqUsername(condition.getUsername()))
                .and(orConditionByEqEmail(condition.getEmail()))
                .and(orConditionByContainsRole(roleTypeList));
//                .and(orConditionByContainsRole(condition.getRoles()));
    }

    private Predicate orConditionByEqMemberId(List<Long> memberId) {
        return orConditions(memberId, member.id::eq);
    }

    private Predicate orConditionByEqUsername(List<String> username) {
        return orConditions(username, member.username::eq);
    }

    private Predicate orConditionByEqEmail(List<String> email) {
        return orConditions(email, member.email::eq);
    }

    private Predicate orConditionByContainsRole(List<RoleType> roleTypes) {
        return orConditions(roleTypes, member.roleTypes::contains);
    }

    private <T> Predicate orConditions(List<T> values, Function<T, BooleanExpression> term) {
        return
                values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }
}
