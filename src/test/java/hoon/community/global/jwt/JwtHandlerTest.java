package hoon.community.global.jwt;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.assertj.core.api.Assertions.*;

class JwtHandlerTest {

    JwtHandler jwtHandler = new JwtHandler();

    @Test
    void createTokenTest() {
        //given, when
        String encodedKey = Base64.getEncoder().encodeToString("myKey".getBytes());
        String token = jwtHandler.createToken(encodedKey, "subject", 60L);

        //then
        assertThat(token).contains("Bearer ");
    }

    @Test
    void extractSubjectTest() {
        //given
        String encodedKey = Base64.getEncoder().encodeToString("myKey".getBytes());
        String subject = "subject";
        String token = jwtHandler.createToken(encodedKey, subject, 60L);

        //when
        String extractedSubject = jwtHandler.extractSubject(encodedKey, token);

        //then
        assertThat(extractedSubject).isEqualTo(subject);
    }

    @Test
    void validateTest() {
        //given
        String encodeKey = Base64.getEncoder().encodeToString("myKey".getBytes());
        String token = jwtHandler.createToken(encodeKey, "subject", 60L);

        //when
        boolean isValid = jwtHandler.validate(encodeKey, token);

        //then
        assertThat(isValid).isTrue();
    }

    @Test
    void invalidateByInvalidKeyTest () {
        //given
        String encodedKey = Base64.getEncoder().encodeToString("myKey".getBytes());
        String token = jwtHandler.createToken(encodedKey, "subject", 60L);

        //when
        boolean isValid = jwtHandler.validate("invalid", token);

        //then
        assertThat(isValid).isFalse();
    }

    @Test
    void invalidateByExpiredTokenTest() {
        //given
        String encodedKey = Base64.getEncoder().encodeToString("myKey".getBytes());
        String token = jwtHandler.createToken(encodedKey, "subject", 0L);

        //when
        boolean isValid = jwtHandler.validate(encodedKey, token);

        //then
        assertThat(isValid).isFalse();
    }

}