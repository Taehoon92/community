package hoon.community.domain.sign.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.junit.jupiter.api.Assertions.*;

class SignInRequestValidationTest {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateTest() {

        //given
        SignInRequest request = createRequest();

        //when
        Set<ConstraintViolation<SignInRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isEmpty();
    }

    @Test
    void invalidateByNotFormattedEmailTest() {

        //given
        String invalidValue = "email";
        SignInRequest request = createRequestWithEmail(invalidValue);

        //when
        Set<ConstraintViolation<SignInRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(Collectors.toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByEmptyEmailTest() {
        //given
        String invalidValue = null;
        SignInRequest request = createRequestWithEmail(invalidValue);

        //when
        Set<ConstraintViolation<SignInRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(Collectors.toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByBlankEmailTest() {
        //given
        String invalidValue = "";
        SignInRequest request = createRequestWithEmail(invalidValue);

        //when
        Set<ConstraintViolation<SignInRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(Collectors.toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByEmptyPasswordTest() {
        //given
        String invalidValue = null;
        SignInRequest request = createRequestWithPassword(invalidValue);

        //when
        Set<ConstraintViolation<SignInRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(Collectors.toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByBlankPasswordTest() {
        //given
        String invalidValue = "";
        SignInRequest request = createRequestWithPassword(invalidValue);

        //when
        Set<ConstraintViolation<SignInRequest>> validate = validator.validate(request);

        //then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(Collectors.toSet())).contains(invalidValue);
    }

    private SignInRequest createRequest() {
        return new SignInRequest("email@email.com", "password1!");
    }

    private SignInRequest createRequestWithEmail(String email) {
        return new SignInRequest(email, "password1!");
    }

    private SignInRequest createRequestWithPassword(String password) {
        return new SignInRequest("email@email.com", password);
    }

}