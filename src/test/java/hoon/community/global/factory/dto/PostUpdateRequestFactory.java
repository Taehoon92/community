package hoon.community.global.factory.dto;

import hoon.community.domain.post.dto.PostCreateRequest;
import hoon.community.domain.post.dto.PostUpdateRequest;
import org.apache.catalina.LifecycleState;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class PostUpdateRequestFactory {

    public static PostUpdateRequest createPostUpdateRequest() {
        return new PostUpdateRequest("title", "content", List.of(), List.of());
    }

    public static PostUpdateRequest createPostUpdateRequest(String title, String content, List<MultipartFile> addedImages, List<Long> deletedImages) {
        return new PostUpdateRequest(title, content, addedImages, deletedImages);
    }

    public static PostUpdateRequest createPostUpdateRequestWithTitle(String title) {
        return new PostUpdateRequest(title, "content", List.of(), List.of());
    }

    public static PostUpdateRequest createPostUpdateRequestWithContent(String content) {
        return new PostUpdateRequest("title", content, List.of(), List.of());
    }


}
