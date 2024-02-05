package com.ttubeog.domain.member.application;

import com.ttubeog.domain.auth.exception.AccessTokenExpiredException;
import com.ttubeog.domain.auth.exception.InvalidAccessTokenException;
import com.ttubeog.domain.auth.security.JwtTokenProvider;
import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.domain.member.dto.request.ProduceNicknameRequest;
import com.ttubeog.domain.member.dto.response.MemberDetailRes;
import com.ttubeog.domain.member.exception.InvalidAccessTokenExpiredException;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import com.ttubeog.global.DefaultAssert;
import com.ttubeog.global.payload.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;


    // 현재 유저 조회
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        Long memberId = jwtTokenProvider.getMemberId(request);

        Optional<Member> checkUser = memberRepository.findById(memberId);
        DefaultAssert.isOptionalPresent(checkUser);
        Member member = checkUser.get();

        MemberDetailRes memberDetailRes = MemberDetailRes.builder()
                .id(member.getId())
                .name(member.getNickname())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(memberDetailRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 닉네임 설정
    @Transactional
    public ResponseEntity<?> postMemberNickname(HttpServletRequest request, ProduceNicknameRequest produceNicknameRequest) {
        Long memberId = jwtTokenProvider.getMemberId(request);
        memberRepository.updateUserNickname(produceNicknameRequest.getNickname(), memberId);

        Optional<Member> checkMember = memberRepository.findById(memberId);

        Member member = checkMember.get();

        MemberDetailRes memberDetailRes = MemberDetailRes.builder()
                .id(member.getId())
                .name(member.getNickname())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(memberDetailRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 토큰 재발급 설정
    public ResponseEntity<?> getMemberReissueToken(HttpServletRequest request) {
        Long memberId;

        try {
            memberId = jwtTokenProvider.getMemberId(request);
        } catch (InvalidAccessTokenException | AccessTokenExpiredException e) {
            throw e;
        }

        Optional<Member> checkMember = memberRepository.findById(memberId);
        if (checkMember.isEmpty()) {
            throw new InvalidMemberException();    // 존재 하지 않는 회원일 경우
        }

        Member member = checkMember.get();

        // 리프레시 토큰으로 새로운 액세스 토큰 발급
        try {
            String newAccessToken = jwtTokenProvider.createAccessToken(memberId);
            return ResponseEntity.ok(new ApiResponse(true, newAccessToken));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new InvalidAccessTokenExpiredException());
        }
    }
}