package com.ttubeog.domain.auth.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class KakaoTokenResponse {

    private String accessToken;
    private String refreshToken;
    private Boolean isRegistered;
}
