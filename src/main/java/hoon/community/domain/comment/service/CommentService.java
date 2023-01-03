package hoon.community.domain.comment.service;

import hoon.community.domain.comment.dto.CommentCreateRequest;
import hoon.community.domain.comment.dto.CommentDto;
import hoon.community.domain.comment.dto.CommentReadCondition;
import hoon.community.domain.comment.entity.Comment;
import hoon.community.domain.comment.repository.CommentRepository;
import hoon.community.domain.member.repository.MemberRepository;
import hoon.community.domain.post.repository.PostRepository;
import hoon.community.global.exception.CustomException;
import hoon.community.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public List<CommentDto> readAll(CommentReadCondition condition) {
        return CommentDto.toDtoList(
            commentRepository.findAllWithMemberAndParentByPostIdOrderByParentIdAscNullsFirstCommentIdAsc(condition.getPostId())
        );
    }

    @Transactional
    public void create(CommentCreateRequest req) {
        commentRepository.save(CommentCreateRequest.toEntity(req, memberRepository, postRepository, commentRepository));
    }

    @Transactional
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        comment.findDeletableComment().ifPresentOrElse(commentRepository::delete, comment::delete);
    }


}
