package hoon.community.domain.post.dto;

import hoon.community.global.factory.dto.PostCreateRequestFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Set;
import java.util.stream.Collectors;

import static hoon.community.global.factory.dto.PostCreateRequestFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PostCreateRequestTest {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateTest() {
        //given
        PostCreateRequest request = createPostCreateRequestWithMemberId(null);

        //when
        Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isEmpty();
    }

    @Test
    void invalidateByEmptyTitleTest() {
        //given
        String invalidValue = null;
        PostCreateRequest request = createPostCreateRequestWithTitle(invalidValue);

        //when
        Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v->v.getInvalidValue()).collect(Collectors.toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByBlankTitleTest() {
        //given
        String invalidValue = " ";
        PostCreateRequest request = createPostCreateRequestWithTitle(invalidValue);

        //when
        Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(Collectors.toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByEmptyContentTest() {
        //given
        String invalidValue = null;
        PostCreateRequest request = createPostCreateRequestWithContent(invalidValue);

        //when
        Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(Collectors.toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByBlankContentTest() {
        //given
        String invalidValue = " ";
        PostCreateRequest request = createPostCreateRequestWithContent(invalidValue);

        //when
        Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(Collectors.toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNotNullMemberIdTest() {
        //given
        Long memberId = 1L;
        PostCreateRequest request = createPostCreateRequestWithMemberId(memberId);

        //when
        Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(Collectors.toSet())).contains(memberId);
    }

}