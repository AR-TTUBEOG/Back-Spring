package com.ttubeog.domain.store.dto.request;

import com.ttubeog.domain.store.domain.StoreType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "매장 등록 Request")
public class RegisterStoreReq {

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

    @Schema(description = "업종")
    private StoreType type;
}
