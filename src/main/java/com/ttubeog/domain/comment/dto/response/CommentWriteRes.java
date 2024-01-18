package com.ttubeog.domain.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
public class CommentWriteRes {

    @Schema(description = "댓글 ID")
    private Long commentId;

    @Schema(description = "작성자 ID")
    private Long memberId;

    @Schema(description = "내용")
    private String content;

    @Schema(description = "위도")
    private Float latitude;

    @Schema(description = "경도")
    private Float longitude;

    @Builder
    public CommentWriteRes(Long commentId, Long memberId, String content, Float latitude, Float longitude) {
        this.commentId = commentId;
        this.memberId = memberId;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
