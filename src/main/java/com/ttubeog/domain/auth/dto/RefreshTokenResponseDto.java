package com.ttubeog.domain.auth.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenResponseDto {
    private String accessToken;
}
