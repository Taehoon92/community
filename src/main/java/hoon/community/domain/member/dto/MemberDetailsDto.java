package hoon.community.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import hoon.community.domain.member.entity.Member;
import hoon.community.domain.role.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDetailsDto {

    private Long id;
    private String email;
    private String username;

    private Set<RoleType> roles;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Australia/Sydney")
    private LocalDateTime createdDate;

    public static MemberDetailsDto toDto(Member member) {
        return new MemberDetailsDto(member.getId(), member.getEmail(), member.getUsername(),
                member.getRoleTypes().stream().collect(Collectors.toSet()),
                member.getCreatedDate());
    }

}
