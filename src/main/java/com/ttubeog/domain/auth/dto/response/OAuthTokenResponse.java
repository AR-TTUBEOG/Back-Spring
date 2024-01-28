package com.ttubeog.domain.auth.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class OAuthTokenResponse {

    private String accessToken;
    private String refreshToken;
    private Boolean isRegistered;
}
