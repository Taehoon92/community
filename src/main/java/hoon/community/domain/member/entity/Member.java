package hoon.community.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import hoon.community.domain.BaseTimeEntity;
import hoon.community.domain.role.entity.Role;
import hoon.community.domain.role.entity.RoleType;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@NamedEntityGraph(
        name = "Member.roles",
        attributeNodes = @NamedAttributeNode(value = "roles", subgraph = "Member.roles.role"),
        subgraphs = @NamedSubgraph(name = "Member.roles.role", attributeNodes = @NamedAttributeNode("role"))
)
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

//    @Column(unique = true)
    private String username;

//    @Column(unique = true)
    private String email;


    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<MemberRole> roles;

    @ElementCollection
    private Set<String> roleTypes;

    public Member(String password, String username, String email, List<Role> roles) {
        this.password = password;
        this.username = username;
        this.email = email;
        this.roles = roles.stream().map(r -> new MemberRole(this, r)).collect(Collectors.toSet());

    }

    public void updateUsername(String username) {
        this.username = username;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
