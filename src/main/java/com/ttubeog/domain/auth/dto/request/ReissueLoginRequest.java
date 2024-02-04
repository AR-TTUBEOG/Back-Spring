package com.ttubeog.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class ReissueLoginRequest {

    @NotBlank
    private String reissueToken;
}
