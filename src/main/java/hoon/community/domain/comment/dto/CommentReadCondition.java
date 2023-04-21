package hoon.community.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentReadCondition {
    @NotNull(message = "Enter a post id")
    @PositiveOrZero(message = "Post id must be zero or positive number")
    private Long postId;

}
