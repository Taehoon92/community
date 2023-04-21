package hoon.community.global.factory.dto;

import hoon.community.domain.post.dto.PostCreateRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class PostCreateRequestFactory {

    public static PostCreateRequest createPostCreateRequest() {
        return new PostCreateRequest("title", "content", 1L, List.of(
                new MockMultipartFile("test1", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes()),
                new MockMultipartFile("test2", "test2.PNG", MediaType.IMAGE_PNG_VALUE, "test2".getBytes()),
                new MockMultipartFile("test3", "test3.PNG", MediaType.IMAGE_PNG_VALUE, "test3".getBytes())
        ));
    }

    public static PostCreateRequest createPostCreateRequest(String title, String content, Long id, List<MultipartFile> images) {
        return new PostCreateRequest(title, content, id, images);
    }

    public static PostCreateRequest createPostCreateRequestWithTitle(String title) {
        return new PostCreateRequest(title, "content", 1L, List.of());
    }

    public static PostCreateRequest createPostCreateRequestWithContent(String content) {
        return new PostCreateRequest("title", content, 1L, List.of());
    }

    public static PostCreateRequest createPostCreateRequestWithMemberId(Long id) {
        return new PostCreateRequest("title", "content", id, List.of());
    }

    public static PostCreateRequest createPostCreateRequestWithImages(List<MultipartFile> images) {
        return new PostCreateRequest("title", "content", 1L, images);
    }


}
