package hoon.community.global.factory.dto;

import hoon.community.domain.post.dto.PostCreateRequest;
import hoon.community.domain.post.dto.PostUpdateRequest;

public class PostUpdateRequestFactory {

    public static PostUpdateRequest createPostUpdateRequest() {
        return new PostUpdateRequest("title", "content");
    }

    public static PostUpdateRequest createPostUpdateRequest(String title, String content) {
        return new PostUpdateRequest(title, content);
    }

    public static PostUpdateRequest createPostUpdateRequestWithTitle(String title) {
        return new PostUpdateRequest(title, "content");
    }

    public static PostUpdateRequest createPostUpdateRequestWithContent(String content) {
        return new PostUpdateRequest("title", content);
    }


}
