package com.ttubeog.domain.image.dto.response;

import com.ttubeog.domain.image.dto.request.ImageRequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ImageResponseDto {

    public Long id;
    public String image;
    public Long placeId;

}
