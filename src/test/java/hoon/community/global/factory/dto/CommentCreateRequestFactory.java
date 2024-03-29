package hoon.community.global.factory.dto;

import hoon.community.domain.comment.dto.CommentCreateRequest;

public class CommentCreateRequestFactory {
    public static CommentCreateRequest createCommentCreateRequest() {
        return new CommentCreateRequest("content", 1L, 1L, null);
    }

    public static CommentCreateRequest createCommentCreateRequest(String content, Long postId, Long memberId, Long parentId) {
        return new CommentCreateRequest(content, postId, memberId, parentId);
    }

    public static CommentCreateRequest createCommentCreateRequestWithContent(String content) {
        return new CommentCreateRequest(content, 1L, 1L, null);
    }

    public static CommentCreateRequest createCommentCreateRequestWithPostId(Long postId) {
        return new CommentCreateRequest("content", postId, 1L, null);
    }

    public static CommentCreateRequest createCommentCreateRequestWithMemberId(Long memberId) {
        return new CommentCreateRequest("content", 1L, memberId, null);
    }

    public static CommentCreateRequest createCommentCreateRequestWithParentId(Long parentId) {
        return new CommentCreateRequest("content", 1L, 1L, parentId);
    }
}
