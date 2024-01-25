package com.ttubeog.domain.store.dto.response;

import com.ttubeog.domain.store.domain.StoreType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Schema(description = "매장 등록 Response")
public class RegisterStoreRes {

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
    private Float latitude;

    @Schema(description = "경도")
    private Float longitude;

    @Schema(description = "이미지")
    private String image;

    @Schema(description = "업종")
    private StoreType type;

    @Builder
    public RegisterStoreRes(Long storeId, Long memberId, String name, String info, Long dongAreaId,
                            String detailAddress,Float latitude, Float longitude, String image, StoreType type) {
        this.storeId = storeId;
        this.memberId = memberId;
        this.name = name;
        this.info = info;
        this.dongAreaId = dongAreaId;
        this.detailAddress = detailAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.type = type;
    }
}
