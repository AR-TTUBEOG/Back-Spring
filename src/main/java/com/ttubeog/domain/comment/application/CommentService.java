package com.ttubeog.domain.comment.application;

import com.ttubeog.domain.comment.domain.Comment;
import com.ttubeog.domain.comment.domain.repository.CommentRepository;
import com.ttubeog.domain.comment.dto.request.GetCommentReq;
import com.ttubeog.domain.comment.dto.request.UpdateCommentReq;
import com.ttubeog.domain.comment.dto.request.WriteCommentReq;
import com.ttubeog.domain.comment.dto.response.GetCommentRes;
import com.ttubeog.domain.comment.dto.response.UpdateCommentRes;
import com.ttubeog.domain.comment.dto.response.WriteCommentRes;
import com.ttubeog.domain.comment.exception.NonExistentCommentException;
import com.ttubeog.domain.comment.exception.UnauthorizedMemberException;
import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import com.ttubeog.global.DefaultAssert;
import com.ttubeog.global.config.security.token.UserPrincipal;
import com.ttubeog.global.payload.ApiResponse;
import com.ttubeog.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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

        Member member = memberRepository.findById(userPrincipal.getId()).orElseThrow(InvalidMemberException::new);

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

        Member member = memberRepository.findById(userPrincipal.getId()).orElseThrow(InvalidMemberException::new);
        Comment comment = commentRepository.findById(updateCommentReq.getCommentId()).orElseThrow(NonExistentCommentException::new);

        Member commentWriter = comment.getMember();
        if (commentWriter.getId() != member.getId()) {
            throw new UnauthorizedMemberException();
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

        memberRepository.findById(userPrincipal.getId()).orElseThrow(InvalidMemberException::new);
        Comment comment = commentRepository.findById(commentId).orElseThrow(NonExistentCommentException::new);
        commentRepository.delete(comment);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("댓글이 정상적으로 삭제되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    // AR뷰를 위한 댓글 조회
    public ResponseEntity<?> getCommentForAR(GetCommentReq getCommentReq) {

        Float userLatitude = getCommentReq.getLatitude();
        Float userLongitude = getCommentReq.getLongitude();

        List<Comment> allComments = getAllComments();
        Double radius = 20.0; // 반경값 확인 필요
        List<GetCommentRes> showComments = new ArrayList<>();

        for (Comment comment : allComments) {

            Float commentLatitude = comment.getLatitude();
            Float commentLongitude = comment.getLongitude();

            double distance = calculateDistance(userLatitude, userLongitude, commentLatitude, commentLongitude);

            if (distance < radius) {
                GetCommentRes getCommentRes = GetCommentRes.builder()
                        .commentId(comment.getId())
                        .memberId(comment.getMember().getId())
                        .content(comment.getContent())
                        .distance(distance)
                        .build();

                showComments.add(getCommentRes);
            }
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(showComments)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 거리 계산
    private double calculateDistance(Float lat1, Float lon1, Float lat2, Float lon2) {
        double R = 6371; // 지구 반지름

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)); // 단위 km

        return R * c;
    }
}
