package com.ttubeog.domain.spot.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class UpdateSpotRequestDto {

    @Schema(description = "산책 스팟 이름", defaultValue = "나의 산책 스팟")
    private String name;

    @Schema(description = "지역 코드 ID", defaultValue = "1")
    private String dongAreaId;

    @Schema(description = "상세 주소", defaultValue = "1층")
    private String detailAddress;

    @Schema(description = "세부 정보", defaultValue = "연인, 친구들과 산책하기 좋은 장소입니다.")
    private String info;

    @Schema(description = "위도", defaultValue = "36.5642886")
    private Double latitude;

    @Schema(description = "경도", defaultValue = "126.9275718")
    private Double longitude;

    @Schema(description = "이미지 리스트")
    private List<String> image;

}
