package hoon.community.global.factory.dto;

import hoon.community.domain.sign.dto.SignInRequest;

public class SignInRequestFactory {
    public static SignInRequest createSignInRequest() {
        return new SignInRequest("member1@member.com", "123456a!");
    }

    public static SignInRequest createSignInRequest(String email, String password) {
        return new SignInRequest(email, password);
    }

    public static SignInRequest SignInRequestWithEmail(String email) {
        return new SignInRequest(email, "123456a!");
    }

    public static SignInRequest SignInRequestWithPassword(String password) {
        return new SignInRequest("member1@member.com", password);
    }
}
