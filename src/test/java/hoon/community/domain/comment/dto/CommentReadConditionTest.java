package hoon.community.domain.comment.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Set;
import java.util.stream.Collectors;

import static hoon.community.global.factory.dto.CommentReadConditionFactory.createCommentReadCondition;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CommentReadConditionTest {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateTest() {
        //given
        CommentReadCondition condition = createCommentReadCondition();

        //when
        Set<ConstraintViolation<CommentReadCondition>> validate = validator.validate(condition);

        //then
        assertThat(validate).isEmpty();

    }

    @Test
    void invalidateByNegativePostIdTest() {
        //given
        Long invalidValue = -1L;
        CommentReadCondition condition = createCommentReadCondition(invalidValue);

        //when
        Set<ConstraintViolation<CommentReadCondition>> validate = validator.validate(condition);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(Collectors.toList())).contains(invalidValue);
    }

}