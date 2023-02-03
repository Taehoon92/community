package hoon.community.domain.member.entity;

import hoon.community.domain.BaseTimeEntity;
import hoon.community.domain.role.entity.RoleType;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @ElementCollection
    private Set<RoleType> roleTypes;

    public Member(String password, String username, String email, List<RoleType> roleTypes) {
        this.password = password;
        this.username = username;
        this.email = email;
        this.roleTypes = new HashSet<>(roleTypes);
    }

    public void updateUsername(String username) {
        this.username = username;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateRoles(Set<RoleType> roleTypes) {
        this.roleTypes = roleTypes;
    }
}
