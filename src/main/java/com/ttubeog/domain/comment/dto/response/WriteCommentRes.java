package com.ttubeog.domain.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
public class WriteCommentRes {

    @Schema(description = "댓글 ID")
    private Long commentId;

    @Schema(description = "작성자 ID")
    private Long memberId;

    @Schema(description = "내용")
    private String content;

    @Schema(description = "위도")
    private Double latitude;

    @Schema(description = "경도")
    private Double longitude;

    @Builder
    public WriteCommentRes(Long commentId, Long memberId, String content, Double latitude, Double longitude) {
        this.commentId = commentId;
        this.memberId = memberId;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
