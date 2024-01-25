package com.ttubeog.domain.auth.dto.apple;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class AppleUser {
    private String email;
    private Name name;
}
