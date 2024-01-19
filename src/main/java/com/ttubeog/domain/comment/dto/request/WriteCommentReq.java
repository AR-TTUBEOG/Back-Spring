package com.ttubeog.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "댓글 작성 Request")
public class WriteCommentReq {

    @Schema(description = "댓글 내용")
    private String content;
    @Schema(description = "위도")
    private Float latitude;
    @Schema(description = "경도")
    private Float longitude;
}
