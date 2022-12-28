package hoon.community.domain.post.dto;

import hoon.community.global.factory.dto.PostUpdateRequestFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.annotation.SecurityTestExecutionListeners;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Set;
import java.util.stream.Collectors;

import static hoon.community.global.factory.dto.PostUpdateRequestFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PostUpdateRequestValidationTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validTest() {
        //given
        PostUpdateRequest request = createPostUpdateRequest("title", "content");

        //when
        Set<ConstraintViolation<PostUpdateRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isEmpty();
    }

    @Test
    void invalidateByEmptyTitleTest() {
        //given
        String invalidValue = null;
        PostUpdateRequest request = createPostUpdateRequestWithTitle(invalidValue);

        //when
        Set<ConstraintViolation<PostUpdateRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(Collectors.toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByBlankTitleTest() {
        //given
        String invalidValue = " ";
        PostUpdateRequest request = createPostUpdateRequestWithTitle(invalidValue);

        //when
        Set<ConstraintViolation<PostUpdateRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(Collectors.toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByEmptyContentTest() {
        //given
        String invalidValue = null;
        PostUpdateRequest request = createPostUpdateRequestWithContent(invalidValue);

        //when
        Set<ConstraintViolation<PostUpdateRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(Collectors.toSet())).contains(invalidValue);

    }

    @Test
    void invalidateByBlankContentTest() {
        //given
        String invalidValue = " ";
        PostUpdateRequest request = createPostUpdateRequestWithContent(invalidValue);

        //when
        Set<ConstraintViolation<PostUpdateRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(Collectors.toSet())).contains(invalidValue);

    }
}