package hoon.community.global.factory.dto;

import hoon.community.domain.post.dto.PostCreateRequest;

public class PostCreateRequestFactory {

    public static PostCreateRequest createPostCreateRequest() {
        return new PostCreateRequest("title", "content", 1L);
    }

    public static PostCreateRequest createPostCreateRequest(String title, String content, Long id) {
        return new PostCreateRequest(title, content, id);
    }

    public static PostCreateRequest createPostCreateRequestWithTitle(String title) {
        return new PostCreateRequest(title, "content", 1L);
    }

    public static PostCreateRequest createPostCreateRequestWithContent(String content) {
        return new PostCreateRequest("title", content, 1L);
    }

    public static PostCreateRequest createPostCreateRequestWithMemberId(Long id) {
        return new PostCreateRequest("title", "content", id);
    }


}
