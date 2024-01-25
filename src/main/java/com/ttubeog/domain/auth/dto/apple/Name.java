package com.ttubeog.domain.auth.dto.apple;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class Name {
    private String firstName;
    private String lastName;
}
