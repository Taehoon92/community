package hoon.community.global.factory.dto;

import hoon.community.domain.comment.entity.Comment;
import hoon.community.domain.member.entity.Member;
import hoon.community.domain.post.entity.Post;
import hoon.community.global.factory.entity.MemberFactory;
import hoon.community.global.factory.entity.PostFactory;

import static hoon.community.global.factory.entity.MemberFactory.createMember;
import static hoon.community.global.factory.entity.PostFactory.createPost;

public class CommentFactory {
    public static Comment createComment(Comment parent) {
        return new Comment("content", createMember(), createPost(), parent);
    }

    public static Comment createDeletedComment(Comment parent) {
        Comment comment = new Comment("content", createMember(), createPost(), parent);
        comment.delete();
        return comment;
    }

    public static Comment createComment(Member member, Post post, Comment parent) {
        return new Comment("content", member, post, parent);
    }
}
