package com.ttubeog.domain.comment.application;

import com.ttubeog.domain.comment.domain.Comment;
import com.ttubeog.domain.comment.domain.repository.CommentRepository;
import com.ttubeog.domain.comment.dto.request.CommentUpdateReq;
import com.ttubeog.domain.comment.dto.request.CommentWriteReq;
import com.ttubeog.domain.comment.dto.response.CommentUpdateRes;
import com.ttubeog.domain.comment.dto.response.CommentWriteRes;
import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.global.DefaultAssert;
import com.ttubeog.global.config.security.token.UserPrincipal;
import com.ttubeog.global.error.DefaultException;
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
    public ResponseEntity<?> writeComment(UserPrincipal userPrincipal, CommentWriteReq commentWriteReq) {

        Optional<Member> optionalMember = memberRepository.findById(userPrincipal.getId());
        DefaultAssert.isOptionalPresent(optionalMember);
        Member member = optionalMember.get();

        Comment comment = Comment.builder()
                .content(commentWriteReq.getContent())
                .latitude(commentWriteReq.getLatitude())
                .longitude(commentWriteReq.getLongitude())
                .member(member)
                .build();

        commentRepository.save(comment);

        CommentWriteRes commentWriteRes = CommentWriteRes.builder()
                .commentId(comment.getId())
                .memberId(member.getId())
                .content(comment.getContent())
                .latitude(comment.getLatitude())
                .longitude(comment.getLongitude())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(commentWriteRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 댓글 수정
    @Transactional
    public ResponseEntity<?> updateComment(UserPrincipal userPrincipal, CommentUpdateReq commentUpdateReq) {

        Optional<Member> optionalMember = memberRepository.findById(userPrincipal.getId());
        DefaultAssert.isOptionalPresent(optionalMember);

        Optional<Comment> optionalComment = commentRepository.findById(commentUpdateReq.getCommentId());
        DefaultAssert.isOptionalPresent(optionalComment);
        Comment comment = optionalComment.get();

        Member commentWriter = comment.getMember();
        if (commentWriter.getId() != optionalMember.get().getId()) {
            DefaultAssert.isTrue(true, "해당 댓글의 작성자만 수정할 수 있습니다.");
        }

        comment.updateContent(commentUpdateReq.getContent());
        CommentUpdateRes commentUpdateRes = CommentUpdateRes.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(commentUpdateRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 댓글 삭제
    @Transactional
    public ResponseEntity<?> deleteComment(UserPrincipal userPrincipal, Long commentId) {

        Optional<Member> optionalMember = memberRepository.findById(userPrincipal.getId());
        DefaultAssert.isOptionalPresent(optionalMember);

        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        DefaultAssert.isOptionalPresent(optionalComment);
        Comment comment = optionalComment.get();

        commentRepository.delete(comment);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("댓글이 정상적으로 삭제되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
