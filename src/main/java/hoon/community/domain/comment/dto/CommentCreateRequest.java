package hoon.community.domain.comment.dto;

import hoon.community.domain.comment.entity.Comment;
import hoon.community.domain.comment.repository.CommentRepository;
import hoon.community.domain.member.repository.MemberRepository;
import hoon.community.domain.post.repository.PostRepository;
import hoon.community.global.exception.CustomException;
import hoon.community.global.exception.ErrorCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.Optional;

@ApiModel(value = "댓글 생성 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateRequest {

    @ApiModelProperty(value = "댓글", notes = "댓글을 입력해주세요.", required = true, example = "my comment")
    @NotBlank(message = "댓글을 입력해주세요.")
    private String content;

    @ApiModelProperty(value = "게시글 id", notes = "게시글 id를 입력해주세요.", example = "7")
    @NotNull(message = "게시글 아이디를 입력해주세요")
    @PositiveOrZero(message = "올바른 게시글 아이디를 입력해주세요")
    private Long postId;

    @ApiModelProperty(hidden = true)
    @Null
    private Long memberId;

    @ApiModelProperty(value = "부모 댓글 id", notes = "부모 댓글 아이디를 입력해주세요", example = "7")
    private Long parentId;

    public static Comment toEntity(CommentCreateRequest request, MemberRepository memberRepository, PostRepository postRepository, CommentRepository commentRepository) {
        return Comment.builder()
                .content(request.getContent())
                .member(memberRepository.findById(request.memberId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)))
                .post(postRepository.findById(request.postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND)))
                .parent(Optional.ofNullable(request.parentId)
                        .map(id -> commentRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND)))
                        .orElse(null)
                ).build();

    }
}
