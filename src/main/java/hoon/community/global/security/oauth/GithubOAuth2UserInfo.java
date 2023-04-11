package hoon.community.global.security.oauth;

import java.util.Map;

public class GithubOAuth2UserInfo extends OAuth2UserInfo{

    public GithubOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        if(attributes.get("id") != null) {
            return attributes.get("id").toString();
        }
        return (String) attributes.get("id");
    }

    @Override
    public String getUsername() {
        return (String) attributes.get("login");
    }

    @Override
    public String getEmail() {
        if(attributes.get("email") != null) {
            return attributes.get("email").toString();
        }
        return attributes.get("id").toString();
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("avatar_url");
    }
}
