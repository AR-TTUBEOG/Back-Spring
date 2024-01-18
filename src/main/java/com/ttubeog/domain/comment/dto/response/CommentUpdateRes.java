package com.ttubeog.domain.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
public class CommentUpdateRes {

    @Schema(description = "댓글 ID")
    private Long commentId;

    @Schema(description = "내용")
    private String content;

    @Builder
    public CommentUpdateRes(Long commentId, String content) {
        this.commentId = commentId;
        this.content = content;
    }
}
