package com.ttubeog.domain.store.dto.request;

import com.ttubeog.domain.store.domain.StoreType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateStoreReq {

    @Schema(description = "매장 ID")
    private Long storeId;

    @Schema(description = "매장 이름")
    private String name;

    @Schema(description = "매장 정보")
    private String info;

    @Schema(description = "상세주소")
    private String detailAddress;

    @Schema(description = "위도")
    private Float latitude;

    @Schema(description = "경도")
    private Float longitude;

    @Schema(description = "이미지")
    private String image;

    @Schema(description = "업종")
    private StoreType type;
}