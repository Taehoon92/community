package hoon.community.domain.member.dto;

import hoon.community.domain.member.entity.Member;
import hoon.community.domain.role.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    private Long id;
    private String email;
    private String username;
    private String mainRole;


    public static MemberDto toDto(Member member) {
        return new MemberDto(member.getId(), member.getEmail(), member.getUsername(), member.getMainRole());
    }
}
