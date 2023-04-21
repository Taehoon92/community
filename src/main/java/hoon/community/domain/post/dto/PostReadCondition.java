package hoon.community.domain.post.dto;

import hoon.community.domain.post.entity.BoardType;
import hoon.community.domain.validation.Enum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "Post read condition")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostReadCondition {

    @ApiModelProperty(value = "Number of page", notes = "Enter a number of page", example = "1")
    @NotNull(message = "Enter a number of page")
    @PositiveOrZero(message = "Page number must be 0 or a positive number")
    private Integer page = 0;

    @ApiModelProperty(value = "Size of page", notes = "Enter a size of page - Posts per page", example = "10")
    @NotNull(message = "Enter a size of page")
    @Positive(message = "Page size must be a positive number")
    private Integer size = 10;

    @ApiModelProperty(value = "Type of board", notes = "Enter a type of board (NOTICE, FORUM)", required = true, example = "NOTICE")
    @Enum(enumClass = BoardType.class, ignoreCase = true)
    @NotNull(message = "Enter a Type of board")
    private List<BoardType> boardType = new ArrayList<>();

    private List<Long> memberId = new ArrayList<>();
    private List<String> username = new ArrayList<>();
    private List<String> content = new ArrayList<>();
    private List<String> title = new ArrayList<>();

}
