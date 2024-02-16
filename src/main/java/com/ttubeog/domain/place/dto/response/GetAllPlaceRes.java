package com.ttubeog.domain.place.dto.response;

import com.ttubeog.domain.place.domain.PlaceType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetAllPlaceRes {

    @Schema(description = "장소 ID")
    private Long placeId;

    @Schema(description = "매장 또는 산책스팟")
    private PlaceType placeType;

    @Schema(description = "지역(동) 이름")
    private String dongName;

    @Schema(description = "등록유저 ID")
    private Long memberId;

    @Schema(description = "장소 이름")
    private String name;

    @Schema(description = "장소 설명")
    private String info;

    @Schema(description = "위도")
    private Double latitude;

    @Schema(description = "경도")
    private Double longitude;

    @Schema(description = "대표 이미지")
    private String image;

    @Schema(description = "별점")
    private Float stars;

    @Schema(description = "방명록 수")
    private Integer guestbookCount;

    @Schema(description = "좋아요 수")
    private Integer likesCount;

    @Schema(description = "좋아요 여부")
    private Boolean isFavorited;

    @Schema(description = "생성 시간")
    private LocalDateTime createdAt;

    @Schema(description = "추천 점수")
    private Integer recommendationScore;

    @Schema(description = "사용자와의 거리")
    private Double distance;

    @Builder
    public GetAllPlaceRes(Long placeId, PlaceType placeType, String dongName, Long memberId, String name, String info, Double latitude, Double longitude,
                          String image, Float stars, Integer guestbookCount, Integer likesCount, Boolean isFavorited,
                          LocalDateTime createdAt, Integer recommendationScore, Double distance) {
        this.placeId = placeId;
        this.placeType = placeType;
        this.dongName = dongName;
        this.memberId = memberId;
        this.name = name;
        this.info = info;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.stars = stars;
        this.guestbookCount = guestbookCount;
        this.likesCount = likesCount;
        this.isFavorited = isFavorited;
        this.createdAt = createdAt;
        this.recommendationScore = recommendationScore;
        this.distance = distance;
    }

    public Integer getLikesCount() {
        return likesCount != null ? likesCount : 0;
    }
}
