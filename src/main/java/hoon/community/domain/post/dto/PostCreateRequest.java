package hoon.community.domain.post.dto;

import hoon.community.domain.member.repository.MemberRepository;
import hoon.community.domain.post.entity.Post;
import hoon.community.global.exception.CustomException;
import hoon.community.global.exception.ErrorCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@ApiModel(value = "게시글 생성 요청")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequest {

    @ApiModelProperty(value = "게시글 제목", notes = "게시글 제목을 입력해주세요", required = true, example = "This is title")
    @NotBlank(message = "게시글 제목을 입력해주세요")
    private String title;

    @ApiModelProperty(value = "게시글 본문", notes = "게시글 본문을 입력해주세요", required = true, example = "This is content")
    @NotBlank(message = "게시글 본문을 입문해주세요")
    private String content;

    @ApiModelProperty(hidden = true)
    @Null
    private Long memberId;

    public static Post toEntity(PostCreateRequest request, MemberRepository memberRepository) {
        return Post.builder()
                .title(request.title)
                .content(request.content)
                .member(memberRepository.findById(request.getMemberId()).orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND)))
                .build();
    }
}
