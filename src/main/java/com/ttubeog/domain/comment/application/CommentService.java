package com.ttubeog.domain.comment.application;

import com.ttubeog.domain.auth.security.JwtTokenProvider;
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
import com.ttubeog.global.payload.CommonDto;
import com.ttubeog.global.payload.Message;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // 댓글 작성
    @Transactional
    public CommonDto writeComment(HttpServletRequest request, WriteCommentReq writeCommentReq) {

        Long memberId = jwtTokenProvider.getMemberId(request);
        Member member = memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);

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

        return new CommonDto(true, writeCommentRes);
    }

    // 댓글 수정
    @Transactional
    public CommonDto updateComment(HttpServletRequest request, UpdateCommentReq updateCommentReq) {

        Long memberId = jwtTokenProvider.getMemberId(request);
        Member member = memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);
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

        return new CommonDto(true, updateCommentRes);
    }

    // 댓글 삭제
    @Transactional
    public CommonDto deleteComment(HttpServletRequest request, Long commentId) {

        Long memberId = jwtTokenProvider.getMemberId(request);
        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);
        Comment comment = commentRepository.findById(commentId).orElseThrow(NonExistentCommentException::new);
        commentRepository.delete(comment);

        return new CommonDto(true, Message.builder().message("댓글이 정상적으로 삭제되었습니다.").build());
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    // AR뷰를 위한 댓글 조회
    public CommonDto getCommentForAR(GetCommentReq getCommentReq) {

        Double userLatitude = getCommentReq.getLatitude();
        Double userLongitude = getCommentReq.getLongitude();

        List<Comment> allComments = getAllComments();
        Double radius = 20.0; // 반경값 확인 필요
        List<GetCommentRes> showComments = new ArrayList<>();

        for (Comment comment : allComments) {

            Double commentLatitude = comment.getLatitude();
            Double commentLongitude = comment.getLongitude();

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

        return new CommonDto(true, showComments);
    }

    // 거리 계산
    private double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
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
