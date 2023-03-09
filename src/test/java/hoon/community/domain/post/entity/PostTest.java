package hoon.community.domain.post.entity;

import hoon.community.domain.post.dto.ImageUpdatedResult;
import hoon.community.domain.post.dto.PostUpdateRequest;
import hoon.community.global.factory.dto.PostUpdateRequestFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

import static hoon.community.global.factory.entity.ImageFactory.createImageWithIdAndOriginName;
import static hoon.community.global.factory.entity.MemberFactory.createMember;
import static hoon.community.global.factory.entity.PostFactory.createPostWithImages;
import static org.assertj.core.api.Assertions.assertThat;

class PostTest {

    @Test
    void updateTest() {
        //given
        Image a = createImageWithIdAndOriginName(1L, "a.jpg");
        Image b = createImageWithIdAndOriginName(2L, "b.jpg");
        Post post = createPostWithImages(createMember(), List.of(a,b));

        //when
        MockMultipartFile cFile = new MockMultipartFile("c", "c.png", MediaType.IMAGE_PNG_VALUE, "cFile".getBytes());
        PostUpdateRequest postUpdateRequest = PostUpdateRequestFactory.createPostUpdateRequest("update title", "update content", List.of(cFile), List.of(a.getId()));
        ImageUpdatedResult imageUpdatedResult = post.update(postUpdateRequest);

        //then
        assertThat(post.getTitle()).isEqualTo(postUpdateRequest.getTitle());
        assertThat(post.getContent()).isEqualTo(postUpdateRequest.getContent());

        List<Image> resultImages = post.getImages();
        List<String> resultOriginNames = resultImages.stream().map(i -> i.getOriginName()).collect(Collectors.toList());
        assertThat(resultImages.size()).isEqualTo(2);
        assertThat(resultOriginNames).contains(b.getOriginName(), cFile.getOriginalFilename());

        List<MultipartFile> addedImageFiles = imageUpdatedResult.getAddedImageFiles();
        assertThat(addedImageFiles.size()).isEqualTo(1);
        assertThat(addedImageFiles.get(0).getOriginalFilename()).isEqualTo(cFile.getOriginalFilename());

        List<Image> addedImages = imageUpdatedResult.getAddedImages();
        List<String> addedOriginNames = addedImages.stream().map(i -> i.getOriginName()).collect(Collectors.toList());
        assertThat(addedImages.size()).isEqualTo(1);
        assertThat(addedOriginNames).contains(cFile.getOriginalFilename());

        List<Image> deletedImages = imageUpdatedResult.getDeletedImages();
        List<String> deletedOriginNames = deletedImages.stream().map(i -> i.getOriginName()).collect(Collectors.toList());
        assertThat(deletedImages.size()).isEqualTo(1);
        assertThat(deletedOriginNames).contains(a.getOriginName());

    }
}