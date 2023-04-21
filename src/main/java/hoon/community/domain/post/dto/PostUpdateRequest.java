package hoon.community.domain.post.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "Post update request")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateRequest {

    @ApiModelProperty(value = "Title of post", notes = "Enter a title of post", required = true, example = "Title")
    @NotBlank(message = "Enter a title of post")
    private String title;

    @ApiModelProperty(value = "Content of post", notes = "Enter contents of post", required = true, example = "Content")
    @NotBlank(message = "Enter contents of post")
    private String content;

    @ApiModelProperty(value = "Added Images", notes = "Add image files")
    private List<MultipartFile> addedImages = new ArrayList<>();

    @ApiModelProperty(value = "Id of deleted images", notes = "Enter ids of deleted image")
    private List<Long> deletedImages = new ArrayList<>();
}
