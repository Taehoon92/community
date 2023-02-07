package hoon.community.domain.member.entity;

import hoon.community.domain.BaseTimeEntity;
import hoon.community.domain.role.entity.RoleType;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
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

    private String mainRole;

    public Member(String password, String username, String email, List<RoleType> roleTypes) {
        this.password = password;
        this.username = username;
        this.email = email;
        this.roleTypes = new HashSet<>(roleTypes);
        this.mainRole = setMainRole(this.roleTypes);
    }

    public void updateUsername(String username) {
        this.username = username;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateRoles(Set<RoleType> roleTypes) {
        this.roleTypes = roleTypes;
        this.mainRole = setMainRole(roleTypes);
    }

    public Member updateModifiedDate() {
        this.onPreUpdate();
        return this;
    }

    public String setMainRole(Set<RoleType> roles) {
        if(roles.contains(RoleType.ROLE_ADMIN)) {
            return "ADMIN";
        } else if(roles.contains(RoleType.ROLE_USER)) {
            return "USER";
        } else if(roles.contains(RoleType.ROLE_SOCIAL)) {
            return "SOCIAL";
        } else {
            return "EMPTY";
        }

    }
}
