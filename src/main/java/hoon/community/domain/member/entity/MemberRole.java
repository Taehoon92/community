package hoon.community.domain.member.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import hoon.community.domain.role.entity.Role;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@IdClass(MemberRoleId.class)
public class MemberRole {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "member_id")
    private Member member;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "role_id")
    private Role role;
}
