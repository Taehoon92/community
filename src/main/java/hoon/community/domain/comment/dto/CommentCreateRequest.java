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

@ApiModel(value = "Comment Create Request")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateRequest {

    @ApiModelProperty(value = "Comment", notes = "Enter a comment", required = true, example = "this is a comment")
    @NotBlank(message = "Enter a comment.")
    private String content;

    @ApiModelProperty(value = "Post id", notes = "Enter a post id", example = "7")
    @NotNull(message = "Enter a post id")
    @PositiveOrZero(message = "Post id must be zero or positive number")
    private Long postId;

    @ApiModelProperty(hidden = true)
    @Null
    private Long memberId;

    @ApiModelProperty(value = "Parent comment id", notes = "Enter a parent comment id", example = "7")
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
