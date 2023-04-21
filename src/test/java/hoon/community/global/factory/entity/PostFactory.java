package hoon.community.global.factory.entity;

import hoon.community.domain.member.entity.Member;
import hoon.community.domain.post.entity.Image;
import hoon.community.domain.post.entity.Post;

import java.util.List;

import static hoon.community.global.factory.entity.MemberFactory.createMember;

public class PostFactory {

    public static Post createPost() {
        return createPost(createMember());
    }

    public static Post createPost(Member member) {
        return new Post("title", "content", member, List.of());
    }

    public static Post createPostWithImages(Member member, List<Image> images) {
        return new Post("title", "content", member, images);
    }

    public static Post createPostWithImages(List<Image> images) {
        return new Post("title", "content", createMember(), images);
    }
}
