package hoon.community.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import hoon.community.domain.member.entity.Member;
import hoon.community.domain.member.entity.MemberRole;
import hoon.community.domain.post.dto.PostInfoDto;
import hoon.community.domain.role.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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

    public static MemberDetailsDto toDto(Member member, Set<RoleType> roles) {
//    public static MemberDetailsDto toDto(Member member, Set<String> roles) {
        return new MemberDetailsDto(member.getId(), member.getEmail(), member.getUsername(), roles, member.getCreatedDate());
    }

    public MemberDetailsDto(Long id, String email, String username, LocalDateTime createdDate) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.createdDate = createdDate;
    }


}
