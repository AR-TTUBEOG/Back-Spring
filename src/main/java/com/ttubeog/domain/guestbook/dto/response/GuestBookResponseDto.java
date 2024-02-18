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

    private String memberName;

    private GuestBookType guestBookType;

    private Long spotId;

    private Long storeId;

    private String content;

    private Float star;

    public GuestBookResponseDto(Long id, Long memberId, String memberName, GuestBookType guestBookType, Long spotId, Long storeId, String content, Float star) {
        this.id = id;
        this.memberId = memberId;
        this.memberName = memberName;
        this.guestBookType = guestBookType;
        this.spotId = spotId;
        this.storeId = storeId;
        this.content = content;
        this.star = star;
    }
}
