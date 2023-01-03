package hoon.community.global.factory.dto;

import hoon.community.domain.comment.dto.CommentReadCondition;

public class CommentReadConditionFactory {
    public static CommentReadCondition createCommentReadCondition() {
        return new CommentReadCondition(1L);
    }

    public static CommentReadCondition createCommentReadCondition(Long postId) {
        return new CommentReadCondition(postId);
    }
}
