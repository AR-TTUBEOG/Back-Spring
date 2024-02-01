package com.ttubeog.domain.image.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UpdateImageRequestDto {

    public Long id;
    public String image;
    public ImageRequestType imageRequestType;
    public Long placeId;
}
