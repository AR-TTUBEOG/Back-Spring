package com.ttubeog.domain.guestbook.dto.request;

import com.ttubeog.domain.guestbook.domain.GuestBookType;
import lombok.Getter;

@Getter
public class CreateGuestBookRequestDto {

    private Long memberId;

    private GuestBookType guestBookType;

    private Long spotId;

    private Long storeId;

    private String content;

    private Float star;

    private String image;

}
