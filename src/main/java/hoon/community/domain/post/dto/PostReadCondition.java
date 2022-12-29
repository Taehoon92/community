package hoon.community.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostReadCondition {
    @NotNull(message = "페이지 번호를 입력해주세요")
    @PositiveOrZero(message = "올바른 페이지 번호를 입력해주세요.")
    private Integer page = 0;

    @NotNull(message = "페이지 크기를 입력해주세요")
    @Positive(message = "올바른 페이지 크기를 입력해주세요.")
    private Integer size = 10;

    private List<Long> memberId = new ArrayList<>();
    private List<String> username = new ArrayList<>();
    private List<String> content = new ArrayList<>();
}
