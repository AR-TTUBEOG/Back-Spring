package com.ttubeog.domain.guestbook.dto.request;

import com.ttubeog.domain.guestbook.domain.GuestBookType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Builder
public class UpdateGuestBookRequestDto {

    private String content;

    private Float star;

    private String image;

    public UpdateGuestBookRequestDto(String content, Float star, String image) {
        this.content = content;
        this.star = star;
        this.image = image;
    }
}
