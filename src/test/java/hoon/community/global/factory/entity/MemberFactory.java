package hoon.community.global.factory.entity;

import hoon.community.domain.member.entity.Member;

import java.util.Collections;
import java.util.List;

public class MemberFactory {

    public static Member createMember() {
        return new Member("password", "testUsername", "email@test.com", Collections.emptyList());
    }

    public static Member createMember(String password, String username, String email){
        return new Member(password, username, email, Collections.emptyList());
    }

    /*
    public static Member createMemberWithRoles(List<Role> roles) {
        return new Member("password", "testUsername", "email@email.com", roles);
    }

     */
}
