package com.ttubeog.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CommentUpdateReq {

    @Schema(description = "댓글 ID")
    private Long commentId;

    @Schema(description = "댓글 내용")
    private String content;
}
