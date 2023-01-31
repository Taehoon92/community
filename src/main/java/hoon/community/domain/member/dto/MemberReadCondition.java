package hoon.community.domain.member.dto;

import hoon.community.domain.member.entity.MemberRole;
import hoon.community.domain.role.entity.Role;
import hoon.community.domain.role.entity.RoleType;
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

    /*
    private Long memberId;
    private String username;
    private String email;
    private Role roles; // Role로 검색하는 기능..?
     */

    private List<Long> memberId = new ArrayList<>();
    private List<String> username = new ArrayList<>();
    private List<String> email = new ArrayList<>();
    private List<String> roles = new ArrayList<>();

}
