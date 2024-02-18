package com.ttubeog.domain.spot.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ttubeog.domain.benefit.domain.BenefitType;
import com.ttubeog.domain.store.domain.StoreType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetSpotDetailRes {

    @Schema(description = "산책 스팟 ID")
    private Long spotId;

    @Schema(description = "등록 유저 ID")
    private Long memberId;

    @Schema(description = "매장 이름")
    private String name;

    @Schema(description = "매장 정보")
    private String info;

    @Schema(description = "지역(동) ID")
    private String dongAreaId;

    @Schema(description = "상세 주소")
    private String detailAddress;

    @Schema(description = "위도")
    private Double latitude;

    @Schema(description = "경도")
    private Double longitude;

    @Schema(description = "이미지 리스트")
    private List<String> image;

    @Schema(description = "별점")
    private Float stars;

    @Schema(description = "방명록 수")
    private Integer guestbookCount;

    @Schema(description = "좋아요 수")
    private Integer likesCount;

    @Schema(description = "현재 유저의 좋아요 여부")
    private Boolean isFavorited;

    @Builder
    public GetSpotDetailRes(Long spotId, Long memberId, String name, String info, String dongAreaId, String detailAddress, Double latitude, Double longitude, List<String> image, Float stars, Integer guestbookCount, Integer likesCount, Boolean isFavorited) {
        this.spotId = spotId;
        this.memberId = memberId;
        this.name = name;
        this.info = info;
        this.dongAreaId = dongAreaId;
        this.detailAddress = detailAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.stars = stars;
        this.guestbookCount = guestbookCount;
        this.likesCount = likesCount;
        this.isFavorited = isFavorited;
    }
}
