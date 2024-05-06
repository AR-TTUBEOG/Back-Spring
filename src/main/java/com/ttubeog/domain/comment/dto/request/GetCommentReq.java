package com.ttubeog.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GetCommentReq {

    @Schema(description = "사용자 위치 위도값")
    private Double latitude;

    @Schema(description = "사용자 위치 경도값")
    private Double longitude;

}
