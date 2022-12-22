package hoon.community.global.factory.entity;

import hoon.community.domain.member.entity.Member;
import hoon.community.domain.post.entity.Post;

public class PostFactory {

    public static Post createPost() {
        return createPost(MemberFactory.createMember());
    }

    public static Post createPost(Member member) {
        return new Post("title", "content", member);
    }
}
