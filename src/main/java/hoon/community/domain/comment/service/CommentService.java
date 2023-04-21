package hoon.community.domain.comment.service;

import hoon.community.domain.comment.dto.CommentCreateRequest;
import hoon.community.domain.comment.dto.CommentDto;
import hoon.community.domain.comment.dto.CommentReadCondition;
import hoon.community.domain.comment.entity.Comment;
import hoon.community.domain.comment.repository.CommentRepository;
import hoon.community.domain.member.repository.MemberRepository;
import hoon.community.domain.post.entity.Post;
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

    /**
    * 게시글에서 댓글 조회할때 controller 에서 pathVariable로 바로 postId받아서 댓글조회하면 됨. 그리고 마이너스가 들어올일이 없으니까..? 있을수도 있긴한데,,
     * 이를 어떻게 처리하면 될지
    *

    public List<CommentDto> readAll(CommentReadCondition condition) {
        return CommentDto.toDtoList(
            commentRepository.findAllWithMemberAndParentByPostIdOrderByParentIdAscNullsFirstCommentIdAsc(condition.getPostId())
        );
    }
     */

    public List<CommentDto> readAll(Long postId) {
        return CommentDto.toDtoList(
            commentRepository.findAllWithMemberAndParentByPostIdOrderByParentIdAscNullsFirstCommentIdAsc(postId)
        );
    }

    @Transactional
    public void create(CommentCreateRequest req) {
        Comment comment = CommentCreateRequest.toEntity(req, memberRepository, postRepository, commentRepository);
        Post post = postRepository.findById(req.getPostId()).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        post.addComment(comment);
        commentRepository.save(comment);
    }

    @Transactional
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        comment.findDeletableComment().ifPresentOrElse(commentRepository::delete, comment::delete);
    }


}
