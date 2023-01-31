package hoon.community.domain.sign.dto;

import org.apache.tomcat.util.bcel.Const;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class SignUpRequestTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateTest() {
        //given
        SignUpRequest request = createRequest();

        //when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isEmpty();
    }

    @Test
    void invalidateByNotFormattedEmailTest() {
        //given
        String invalidValue = "email";
        SignUpRequest request = createRequestWithEmail(invalidValue);

        //when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByEmptyEmailTest() {
        //given
        String invalidValue = null;
        SignUpRequest request = createRequestWithEmail(invalidValue);

        //when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByBlankEmailTest() {
        //given
        String invalidValue = "";
        SignUpRequest request = createRequestWithEmail(invalidValue);

        //when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByEmptyPasswordTest() {
        //given
        String invalidValue = null;
        SignUpRequest request = createRequestWithPassword(invalidValue);

        //when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByBlankPasswordTest() {
        //given
        String invalidValue = "     ";
        SignUpRequest request = createRequestWithPassword(invalidValue);

        //when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByShortPasswordTest() {
        //given
        String invalidValue = "pass1!";
        SignUpRequest request = createRequestWithPassword(invalidValue);

        //when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNonAlphabetPasswordTest() {
        //given
        String invalidValue = "1212!@1212";
        SignUpRequest request = createRequestWithPassword(invalidValue);

        //when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNonNumberPasswordTest() {
        //given
        String invalidValue = "password!@";
        SignUpRequest request = createRequestWithPassword(invalidValue);

        //when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNonSpecialCasePasswordTest() {
        //given
        String invalidValue = "password12";
        SignUpRequest request = createRequestWithPassword(invalidValue);

        //when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByEmptyUsernameTest() {
        //given
        String invalidValue = null;
        SignUpRequest request = createRequestWithUsername(invalidValue);

        //when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByBlankUsernameTest() {
        //given
        String invalidValue = "    ";
        SignUpRequest request = createRequestWithUsername(invalidValue);

        //when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByShortUsernameTest() {
        //given
        String invalidValue = "a";
        SignUpRequest request = createRequestWithUsername(invalidValue);

        //when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNotEnglishUsernameTest() {
        //given
        String invalidValue = "테스트";
        SignUpRequest request = createRequestWithUsername(invalidValue);

        //when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNumberUsernameTest() {
        //given
        String invalidValue = "123123";
        SignUpRequest request = createRequestWithUsername(invalidValue);

        //when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isEmpty();
    }







    private SignUpRequest createRequest() {
        return new SignUpRequest("password1!", "username", "email@email.com");
    }

    private SignUpRequest createRequestWithEmail(String email) {
        return new SignUpRequest("password1!", "username", email);
    }

    private SignUpRequest createRequestWithPassword(String password) {
        return new SignUpRequest(password, "username", "email@email.com");
    }

    private SignUpRequest createRequestWithUsername(String username) {
        return new SignUpRequest("password1!", username, "email@email.com");
    }
}