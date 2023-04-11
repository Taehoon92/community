package hoon.community.global.security.oauth;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo{

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getUsername() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        if (properties == null) {
            return null;
        }
        return (String) properties.get("nickname");
    }

//    @Override
//    public String getEmail() {
//        return (String) attributes.get("account_email");
//    }

    //이메일 제공 필수가 아니라서 일단은 이메일 있으면 이메일, 없으면 id 넘버 이용
    @Override
    public String getEmail() {

        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        if((boolean) kakaoAccount.get("has_email")) {
            return (String) kakaoAccount.get("email");
        }

        return attributes.get("id").toString();

    }

    @Override
    public String getImageUrl() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        if (properties == null) {
            return null;
        }

        return (String) properties.get("thumbnail_image");
    }
}
