package hoon.community.domain.post.entity;

import hoon.community.global.exception.CustomException;
import hoon.community.global.factory.entity.PostFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static hoon.community.global.factory.entity.ImageFactory.createImage;
import static hoon.community.global.factory.entity.ImageFactory.createImageWithOriginName;
import static hoon.community.global.factory.entity.PostFactory.createPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class ImageTest {

    @Test
    void createImageTest() {
        //given
        String validExtension = "JPEG";

        //when, then
        createImageWithOriginName("image." + validExtension);
    }

    @Test
    void createImageExceptionByUnsupportedFormatTest() {
        //given
        String invalidExtension = "invalid";

        //when, then
        assertThatThrownBy(() -> createImageWithOriginName("image." + invalidExtension))
                .isInstanceOf(CustomException.class);
    }

    @Test
    void createImageExceptionByNoneExtensionTest() {
        //given
        String originName = "image";

        //when, then
        assertThatThrownBy(() -> createImageWithOriginName(originName))
                .isInstanceOf(CustomException.class);
    }

    @Test
    void initPostTest() {
        //given
        Image image = createImage();

        //when
        Post post = createPost();
        image.initPost(post);

        //then
        assertThat(image.getPost()).isSameAs(post);
    }

    @Test
    void initPostNotChangedTest() {
        //given
        Image image = createImage();
        image.initPost(createPost());

        //when
        Post post = createPost();
        image.initPost(post);

        //then
        assertThat(image.getPost()).isNotSameAs(post);

    }
}