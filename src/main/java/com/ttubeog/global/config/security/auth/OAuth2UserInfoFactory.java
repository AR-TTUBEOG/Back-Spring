package com.ttubeog.global.config.security.auth;

import com.ttubeog.domain.user.domain.Provider;
import com.ttubeog.global.DefaultAssert;
import com.ttubeog.global.config.security.auth.company.Kakao;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
//        if(registrationId.equalsIgnoreCase(Provider.google.toString())) {
//            return new Google(attributes);
//        } else if (registrationId.equalsIgnoreCase(Provider.facebook.toString())) {
//            return new Facebook(attributes);
//        } else if (registrationId.equalsIgnoreCase(Provider.github.toString())) {
//            return new Github(attributes);
//        } else if (registrationId.equalsIgnoreCase(Provider.naver.toString())) {
//            return new Naver(attributes);
//        } else if ~~
       if (registrationId.equalsIgnoreCase(Provider.kakao.toString())) {
           return new Kakao(attributes);
       } else if (registrationId.equalsIgnoreCase(Provider.apple.toString())) {
           DefaultAssert.isAuthentication("애플 로그인은 구현 예정입니다.");
        } else {
            DefaultAssert.isAuthentication("해당 oauth2 기능은 지원하지 않습니다.");
        }
        return null;
    }
}
