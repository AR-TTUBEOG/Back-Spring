package com.ttubeog.domain.comment.application;

import com.ttubeog.domain.comment.domain.Comment;
import com.ttubeog.domain.comment.domain.repository.CommentRepository;
import com.ttubeog.domain.comment.dto.request.UpdateCommentReq;
import com.ttubeog.domain.comment.dto.request.WriteCommentReq;
import com.ttubeog.domain.comment.dto.response.UpdateCommentRes;
import com.ttubeog.domain.comment.dto.response.WriteCommentRes;
import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.global.DefaultAssert;
import com.ttubeog.global.config.security.token.UserPrincipal;
import com.ttubeog.global.payload.ApiResponse;
import com.ttubeog.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    // 댓글 작성
    @Transactional
    public ResponseEntity<?> writeComment(UserPrincipal userPrincipal, WriteCommentReq writeCommentReq) {

        Optional<Member> memberOptional = memberRepository.findById(userPrincipal.getId());
        DefaultAssert.isOptionalPresent(memberOptional);
        Member member = memberOptional.get();

        Comment comment = Comment.builder()
                .content(writeCommentReq.getContent())
                .latitude(writeCommentReq.getLatitude())
                .longitude(writeCommentReq.getLongitude())
                .member(member)
                .build();

        commentRepository.save(comment);

        WriteCommentRes writeCommentRes = WriteCommentRes.builder()
                .commentId(comment.getId())
                .memberId(member.getId())
                .content(comment.getContent())
                .latitude(comment.getLatitude())
                .longitude(comment.getLongitude())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(writeCommentRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 댓글 수정
    @Transactional
    public ResponseEntity<?> updateComment(UserPrincipal userPrincipal, UpdateCommentReq updateCommentReq) {

        Optional<Member> memberOptional = memberRepository.findById(userPrincipal.getId());
        DefaultAssert.isOptionalPresent(memberOptional);

        Optional<Comment> commentOptional = commentRepository.findById(updateCommentReq.getCommentId());
        DefaultAssert.isOptionalPresent(commentOptional);
        Comment comment = commentOptional.get();

        Member commentWriter = comment.getMember();
        if (commentWriter.getId() != memberOptional.get().getId()) {
            DefaultAssert.isTrue(true, "해당 댓글의 작성자만 수정할 수 있습니다.");
        }

        comment.updateContent(updateCommentReq.getContent());
        UpdateCommentRes updateCommentRes = UpdateCommentRes.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(updateCommentRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 댓글 삭제
    @Transactional
    public ResponseEntity<?> deleteComment(UserPrincipal userPrincipal, Long commentId) {

        Optional<Member> memberOptional = memberRepository.findById(userPrincipal.getId());
        DefaultAssert.isOptionalPresent(memberOptional);

        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        DefaultAssert.isOptionalPresent(commentOptional);
        Comment comment = commentOptional.get();

        commentRepository.delete(comment);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("댓글이 정상적으로 삭제되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 댓글 조회

}
