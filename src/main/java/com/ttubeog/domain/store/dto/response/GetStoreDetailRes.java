package com.ttubeog.domain.store.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ttubeog.domain.benefit.domain.BenefitType;
import com.ttubeog.domain.store.domain.StoreType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetStoreDetailRes {

    @Schema(description = "매장 ID")
    private Long storeId;

    @Schema(description = "등록 유저 ID")
    private Long memberId;

    @Schema(description = "매장 이름")
    private String name;

    @Schema(description = "매장 정보")
    private String info;

    @Schema(description = "지역(동) ID")
    private Long dongAreaId;

    @Schema(description = "상세 주소")
    private String detailAddress;

    @Schema(description = "위도")
    private Double latitude;

    @Schema(description = "경도")
    private Double longitude;

    @Schema(description = "이미지")
    private String image;

    @Schema(description = "별점")
    private Float stars;

    @Schema(description = "업종")
    private StoreType type;

    @Schema(description = "혜택 목록")
    private List<BenefitType> storeBenefits;

    @Schema(description = "방명록 수")
    private Integer guestbookCount;

    @Schema(description = "좋아요 수")
    private Integer likesCount;
}
