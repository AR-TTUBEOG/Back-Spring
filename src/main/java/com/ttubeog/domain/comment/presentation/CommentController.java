package com.ttubeog.domain.comment.presentation;

import com.ttubeog.domain.comment.application.CommentService;
import com.ttubeog.domain.comment.dto.request.GetCommentReq;
import com.ttubeog.domain.comment.dto.request.UpdateCommentReq;
import com.ttubeog.domain.comment.dto.request.WriteCommentReq;
import com.ttubeog.domain.comment.dto.response.GetCommentRes;
import com.ttubeog.domain.comment.dto.response.UpdateCommentRes;
import com.ttubeog.domain.comment.dto.response.WriteCommentRes;
import com.ttubeog.global.config.security.token.CurrentUser;
import com.ttubeog.global.config.security.token.UserPrincipal;
import com.ttubeog.global.payload.ErrorResponse;
import com.ttubeog.global.payload.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Comment", description = "Comment API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @Operation(summary = "댓글 작성", description = "댓글을 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 작성 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = WriteCommentRes.class))}),
            @ApiResponse(responseCode = "400", description = "댓글 작성 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping
    public ResponseEntity<?> writeComment(
            HttpServletRequest request,
            @Valid @RequestBody WriteCommentReq writeCommentReq
    ) {
        return commentService.writeComment(request, writeCommentReq);
    }

    // 댓글 수정
    @Operation(summary = "댓글 수정", description = "댓글 내용을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UpdateCommentRes.class))}),
            @ApiResponse(responseCode = "400", description = "댓글 수정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PatchMapping
    public ResponseEntity<?> updateComment(
            HttpServletRequest request,
            @Valid @RequestBody UpdateCommentReq updateCommentReq
    ) {
        return commentService.updateComment(request, updateCommentReq);
    }

    // 댓글 삭제
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "댓글 삭제 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            HttpServletRequest request,
            @PathVariable Long commentId
    ) {
        return commentService.deleteComment(request, commentId);
    }

    // AR뷰 댓글 조회 (반경 이용)
    @Operation(summary = "댓글 조회", description = "반경 내의 댓글을 조회힙니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetCommentRes.class))}),
            @ApiResponse(responseCode = "400", description = "댓글 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping
    public ResponseEntity<?> getCommentForAR(
            @Valid GetCommentReq getCommentReq
    ) {
        return commentService.getCommentForAR(getCommentReq);
    }
}
