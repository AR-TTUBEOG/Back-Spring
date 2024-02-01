package com.ttubeog.domain.spot.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CreateSpotRequestDto {

    private Long id;
    private String name;
    private Long dongAreaId;
    private String detailAddress;
    private String info;
    private Float latitude;
    private Float longitude;
    private List<String> image;

}
