package com.ttubeog.domain.user.dto.response;

import com.ttubeog.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailRes {

    private Long id;

    private String name;

    private String email;

    private String ImgUrl;

    public static UserDetailRes toDto(User user) {
        return UserDetailRes.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .ImgUrl(user.getImageUrl())
                .build();
    }

}
