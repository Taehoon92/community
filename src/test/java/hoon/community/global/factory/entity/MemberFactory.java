package hoon.community.global.factory.entity;

import hoon.community.domain.member.entity.Member;
import hoon.community.domain.role.entity.Role;

import java.util.Collections;
import java.util.List;

public class MemberFactory {

    public static Member createMember() {
        return new Member("testId", "password", "testUsername", "email@test.com", Collections.emptyList());
    }

    public static Member createMember(String loginId, String password, String username, String email){
        return new Member(loginId, password, username, email, Collections.emptyList());
    }

    public static Member createMemberWithRoles(List<Role> roles) {
        return new Member("testId", "password", "testUsername", "email@email.com", roles);
    }
}
