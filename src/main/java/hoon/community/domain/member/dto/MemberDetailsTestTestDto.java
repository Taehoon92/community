package hoon.community.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import hoon.community.domain.member.entity.Member;
import hoon.community.domain.member.entity.MemberRole;
import hoon.community.domain.role.entity.Role;
import hoon.community.domain.role.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDetailsTestTestDto {

    private Long id;
    private String email;
    private String username;
    private Set<RoleType> roles;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Australia/Sydney")
    private LocalDateTime createdDate;


    public static MemberDetailsTestTestDto toDto(Member member) {

        return new MemberDetailsTestTestDto(member.getId(), member.getEmail(), member.getUsername(),
                member.getRoles().stream().map(memberRole -> memberRole.getRole())
                        .map(role -> role.getRoleType()).collect(Collectors.toSet())
                , member.getCreatedDate());
    }

}
