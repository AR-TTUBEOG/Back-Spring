package com.ttubeog.domain.spot.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CreateSpotResponseDto {

    private Long id;
    private String name;
    private Long memberId;
    private Long dongAreaId;
    private String detailAddress;
    private String info;
    private Float latitude;
    private Float longitude;
    private List<String> image;
    private Float stars;

}
