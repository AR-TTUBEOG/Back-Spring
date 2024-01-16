package com.ttubeog.domain.auth.application;


import com.ttubeog.domain.auth.dto.KakaoInfoDto;
import com.ttubeog.domain.member.application.MemberService;
import com.ttubeog.domain.member.dto.response.MemberDetailRes;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class KakaoOauthService {
    private final MemberService memberService;

    // 카카오 API를 호출해서 AccessToken으로 멤버 정보를 가져오는 로직
    public Map<String, Object> getMemberInfoByToken(String accessToken) {
        return WebClient.create()
                .get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(httpHeaders -> httpHeaders.setBasicAuth(accessToken))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();
    }


    // 카카오 API에서 가져온 멤버 정보를 DB 저장, 업데이트
    public MemberDetailRes getMemberProfileByToken(String accessToken) {
        Map<String, Object> memberInfoByToken = getMemberInfoByToken(accessToken);
        KakaoInfoDto kakaoInfoDto = new KakaoInfoDto(memberInfoByToken);
        MemberDetailRes memberDetailRes = MemberDetailRes.builder()
                .id(kakaoInfoDto.getId())
                .email(kakaoInfoDto.getEmail())
                .build();

        if(memberService.findById(memberDetailRes.getId()).isPresent()) {
            memberService.update(memberDetailRes);
        } else {
            memberService.save(memberDetailRes);
        }
        return memberDetailRes;
    }
}
