package com.ttubeog.domain.comment.presentation;

import com.ttubeog.domain.comment.application.CommentService;
import com.ttubeog.domain.comment.dto.request.CommentUpdateReq;
import com.ttubeog.domain.comment.dto.request.CommentWriteReq;
import com.ttubeog.domain.comment.dto.response.CommentUpdateRes;
import com.ttubeog.domain.comment.dto.response.CommentWriteRes;
import com.ttubeog.global.config.security.token.CurrentUser;
import com.ttubeog.global.config.security.token.UserPrincipal;
import com.ttubeog.global.payload.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Comment", description = "Comment API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @Operation(summary = "댓글 작성", description = "댓글을 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 작성 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommentWriteRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "댓글 작성 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } )
    })
    @PostMapping
    public ResponseEntity<?> writeComment(
            @Parameter(description = "AccessToken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody CommentWriteReq commentWriteReq
    ) {
        return commentService.writeComment(userPrincipal, commentWriteReq);
    }

    // 댓글 수정
    @Operation(summary = "댓글 수정", description = "댓글 내용을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommentUpdateRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "댓글 수정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } )
    })
    @PatchMapping
    public ResponseEntity<?> updateComment(
            @Parameter(description = "AccessToken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody CommentUpdateReq commentUpdateReq
    ) {
        return commentService.updateComment(userPrincipal, commentUpdateReq);
    }

    // 댓글 삭제


}
