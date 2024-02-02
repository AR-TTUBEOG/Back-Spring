package com.ttubeog.domain.image.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateImageRequestDto {

    public String image;
    public ImageRequestType imageRequestType;
    public Long placeId;

}
