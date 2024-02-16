package com.ttubeog.domain.place.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SearchPlaceReq {

    @Schema(description = "검색어")
    private String keyword;

    @Schema(description = "지역(동) 이름")
    private String dongName;
}
