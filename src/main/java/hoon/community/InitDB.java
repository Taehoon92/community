package hoon.community;

import hoon.community.domain.comment.dto.CommentCreateRequest;
import hoon.community.domain.comment.entity.Comment;
import hoon.community.domain.comment.repository.CommentRepository;
import hoon.community.domain.comment.service.CommentService;
import hoon.community.domain.member.entity.Member;
import hoon.community.domain.member.repository.MemberRepository;
import hoon.community.domain.post.entity.Post;
import hoon.community.domain.post.repository.PostRepository;
import hoon.community.domain.role.entity.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitDB {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private final CommentService commentService;

//    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initDB() {
        log.info("initialize database");

        initRole();
        initMemberAdmin();
        initMemberTester();
        initPost();
        initComment();

        log.info("initialized database");

    }

    private void initRole() {
//        roleRepository.saveAll(
//                List.of(RoleType.values()).stream().map(roleType -> new Role(roleType)).collect(Collectors.toSet())
//        );
    }

    private void initMemberAdmin() {
        memberRepository.save(
                new Member(passwordEncoder.encode("password1!"), "admin", "admin@admin.com",
                        List.of(RoleType.ROLE_USER, RoleType.ROLE_ADMIN)
                )
        );
    }

    private void initMemberTester() {
        IntStream.range(1,10)
                        .forEach(i -> memberRepository.save(
                                new Member(passwordEncoder.encode("password1!"), "tester" +i, "tester"+i+"@tester.com",
                                        List.of(RoleType.ROLE_USER))
                        ));
        IntStream.range(11,15)
                .forEach(i -> memberRepository.save(
                        new Member(passwordEncoder.encode("password1!"), "tester" +i, "tester"+i+"@tester.com",
                                List.of(RoleType.ROLE_USER, RoleType.ROLE_SOCIAL))
                ));
        /*
        memberRepository.saveAll(
                List.of(
                        new Member(passwordEncoder.encode("password1!"), "tester1", "tester1@tester.com",
                                List.of(roleRepository.findByRoleType(RoleType.ROLE_USER).orElseThrow(()-> new CustomException(ErrorCode.ROLE_NOT_FOUND)))),
                        new Member(passwordEncoder.encode("password1!"), "tester2", "tester2@tester.com",
                                List.of(roleRepository.findByRoleType(RoleType.ROLE_USER).orElseThrow(()-> new CustomException(ErrorCode.ROLE_NOT_FOUND))))
                )
        );

         */
    }

    private void initPost() {
        Member member1 = memberRepository.findAll().get(0);
        Member member2 = memberRepository.findAll().get(1);
        IntStream.range(0, 100)
                .forEach(i -> postRepository.save(
                        new Post("title" + i, "content" + i, member1)
                ));
        IntStream.range(100, 200)
                .forEach(i -> postRepository.save(
                        new Post("title " + i, "content" + i, member2)

                ));
    }

    private void initComment() {
        Member member1 = memberRepository.findAll().get(0);
        Member member2 = memberRepository.findAll().get(1);
        Post post = postRepository.findAll().get(0);
        commentService.create(new CommentCreateRequest("content1",post.getId(),member1.getId(),null));
        commentService.create(new CommentCreateRequest("content2",post.getId(),member1.getId(),1L));
        commentService.create(new CommentCreateRequest("content3",post.getId(),member2.getId(),1L));
        commentService.create(new CommentCreateRequest("content4",post.getId(),member1.getId(),2L));
        commentService.create(new CommentCreateRequest("content5",post.getId(),member1.getId(),2L));
        commentService.create(new CommentCreateRequest("content6",post.getId(),member1.getId(),4L));
        commentService.create(new CommentCreateRequest("content7",post.getId(),member2.getId(),3L));
        commentService.create(new CommentCreateRequest("content8",post.getId(),member2.getId(),null));
//        Comment c1 = commentRepository.save(new Comment("content", member1, post, null));
//        Comment c2 = commentRepository.save(new Comment("content", member1, post, c1));
//        Comment c3 = commentRepository.save(new Comment("content", member2, post, c1));
//        Comment c4 = commentRepository.save(new Comment("content", member1, post, c2));
//        Comment c5 = commentRepository.save(new Comment("content", member1, post, c2));
//        Comment c6 = commentRepository.save(new Comment("content", member2, post, c4));
//        Comment c7 = commentRepository.save(new Comment("content", member2, post, c3));
//        Comment c8 = commentRepository.save(new Comment("content", member2, post, null));

    }
}
