package com.ttubeog.domain.guestbook.dto.request;

import com.ttubeog.domain.guestbook.domain.GuestBookType;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
public class CreateGuestBookRequestDto {

    private GuestBookType guestBookType;

    private Long spotId;

    private Long storeId;

    private String content;

    private Float star;

}
