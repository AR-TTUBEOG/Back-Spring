package com.ttubeog.domain.spot.dto.response;


import com.ttubeog.domain.image.application.ImageService;
import com.ttubeog.domain.spot.domain.Spot;
import com.ttubeog.global.payload.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.ttubeog.domain.image.application.ImageService.getImageString;

@Getter
@Builder
@AllArgsConstructor
public class SpotResponseDto {

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
