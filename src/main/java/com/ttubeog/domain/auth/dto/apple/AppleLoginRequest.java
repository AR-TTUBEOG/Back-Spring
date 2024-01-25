package com.ttubeog.domain.auth.dto.apple;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class AppleLoginRequest {
//    private String state;
//    private String authorizationCode;
    @NotBlank(message = "1012: 공백일 수 없습니다.")
    private String identityToken;
//    private AppleUser appleUser;
}
