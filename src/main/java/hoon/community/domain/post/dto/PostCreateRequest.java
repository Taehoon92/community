package hoon.community.domain.post.dto;

import hoon.community.domain.member.repository.MemberRepository;
import hoon.community.domain.post.entity.BoardType;
import hoon.community.domain.post.entity.Image;
import hoon.community.domain.post.entity.Post;
import hoon.community.global.exception.CustomException;
import hoon.community.global.exception.ErrorCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApiModel(value = "게시글 생성 요청")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequest {

    @ApiModelProperty(value = "Post title", notes = "Enter title of post", required = true, example = "This is title")
    @NotBlank(message = "Enter title of post")
    private String title;

    @ApiModelProperty(value = "Post content", notes = "Enter content of post", required = true, example = "This is content")
    @NotBlank(message = "Enter title of post")
    private String content;

    @ApiModelProperty(hidden = true)
    @Null
    private Long memberId;

    @ApiModelProperty(value = "Image", notes = "Attach images")
    private List<MultipartFile> images = new ArrayList<>();

    public static Post toEntity(PostCreateRequest request, MemberRepository memberRepository, BoardType boardType) {
        return Post.builder()
                .title(request.title)
                .content(request.content)
                .boardType(boardType)
                .member(memberRepository.findById(request.getMemberId()).orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND)))
                .images(request.images.stream().map(i -> new Image(i.getOriginalFilename())).collect(Collectors.toList()))
                .build();
    }
}
