package hoon.community.global.factory.entity;

import hoon.community.domain.member.entity.Member;
import hoon.community.domain.member.entity.Role;

import static java.util.Collections.emptyList;

public class MemberFactory {

    public static Member createMember() {
        return new Member("testId", "password", "testUsername", "email@test.com", Role.USER);
    }

    public static Member createMember(String loginId, String password, String username, String email){
        return new Member(loginId, password, username, email, Role.USER);
    }
}
