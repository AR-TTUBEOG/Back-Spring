package com.ttubeog.domain.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
public class GetCommentRes {

    @Schema(description = "댓글 ID")
    private Long commentId;

    @Schema(description = "작성자 ID")
    private Long memberId;

    @Schema(description = "내용")
    private String content;

    @Schema(description = "사용자와 해당 댓글 간 거리")
    private Double distance;

    @Builder
    public GetCommentRes(Long commentId, Long memberId, String content, Double distance) {
        this.commentId = commentId;
        this.memberId = memberId;
        this.content = content;
        this.distance = distance;
    }

}
