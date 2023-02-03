package hoon.community.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberReadCondition {

    @PositiveOrZero
    private Integer page = 0;

    @Positive
    private Integer size = 10;

    private List<Long> memberId = new ArrayList<>();
    private List<String> username = new ArrayList<>();
    private List<String> email = new ArrayList<>();
    private List<String> role = new ArrayList<>();

}
