package com.ttubeog.domain.guestbook.dto.response;

import com.ttubeog.domain.guestbook.domain.GuestBookType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class GuestBookResponseDto {

    private  Long id;

    private Long memberId;

    private GuestBookType guestBookType;

    private Long spotId;

    private Long storeId;

    private String content;

    private Float star;

    private String image;


    public GuestBookResponseDto(Long id, Long memberId, GuestBookType guestBookType, Long spotId, Long storeId, String content, Float star, String image) {
        this.id = id;
        this.memberId = memberId;
        this.guestBookType = guestBookType;
        this.spotId = spotId;
        this.storeId = storeId;
        this.content = content;
        this.star = star;
        this.image = image;
    }
}
