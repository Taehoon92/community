package hoon.community.domain.post.dto;

import hoon.community.global.factory.dto.PostReadConditionFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Set;
import java.util.stream.Collectors;

import static hoon.community.global.factory.dto.PostReadConditionFactory.createPostReadCondition;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PostReadConditionValidationTest {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateTest() {
        //given
        PostReadCondition condition = createPostReadCondition(1, 1);

        //when
        Set<ConstraintViolation<PostReadCondition>> validate = validator.validate(condition);

        //then
        assertThat(validate).isEmpty();
    }

    @Test
    void invalidateByNullPageTest() {
        //given
        Integer invalidValue = null;
        PostReadCondition condition = createPostReadCondition(invalidValue, 1);

        //when
        Set<ConstraintViolation<PostReadCondition>> validate = validator.validate(condition);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(Collectors.toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNegativePageTest() {
        //given
        Integer invalidValue = -1;
        PostReadCondition condition = createPostReadCondition(invalidValue, 1);

        //when
        Set<ConstraintViolation<PostReadCondition>> validate = validator.validate(condition);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(Collectors.toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNullSizeTest() {
        //given
        Integer invalidValue = null;
        PostReadCondition condition = createPostReadCondition(1, invalidValue);

        //when
        Set<ConstraintViolation<PostReadCondition>> validate = validator.validate(condition);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(Collectors.toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByZeroSizeTest() {
        //given
        Integer invalidValue = 0;
        PostReadCondition condition = createPostReadCondition(1, invalidValue);

        //when
        Set<ConstraintViolation<PostReadCondition>> validate = validator.validate(condition);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(Collectors.toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNegativeSizeTest() {
        //given
        Integer invalidValue = -1;
        PostReadCondition condition = createPostReadCondition(1, invalidValue);

        //when
        Set<ConstraintViolation<PostReadCondition>> validate = validator.validate(condition);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(Collectors.toSet())).contains(invalidValue);
    }
}