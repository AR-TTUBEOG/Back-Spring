package com.ttubeog.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class KakaoInfoDto {
    private Long id;
    private String email;

    public KakaoInfoDto(Map<String, Object> info) {
        this.id = info.get("id") != null ? Long.valueOf(info.get("id").toString()) : null;
        this.email = info.get("email") != null
                ? info.get("email").toString() : "";
    }
}
