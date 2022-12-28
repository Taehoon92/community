package hoon.community.domain.post.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "게시글 수정 요청")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateRequest {

    @ApiModelProperty(value = "게시글 제목", notes = "게시글 제목을 입력해주세요", required = true, example = "Title")
    @NotBlank(message = "게시글 제목을 입력해주세요")
    private String title;

    @ApiModelProperty(value = "게시글 본문", notes = "게시글 본문을 입력해주세요", required = true, example = "Content")
    @NotBlank(message = "게시글 본문을 입력해주세요")
    private String content;
}
