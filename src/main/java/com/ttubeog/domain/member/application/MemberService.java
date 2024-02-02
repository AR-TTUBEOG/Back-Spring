package com.ttubeog.domain.member.application;

import com.ttubeog.domain.auth.config.SecurityUtil;
import com.ttubeog.domain.auth.security.JwtTokenProvider;
import com.ttubeog.domain.member.dto.MemberDto;
import com.ttubeog.domain.member.dto.response.MemberDetailRes;
import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.domain.member.mapper.MemberMapper;
import com.ttubeog.global.DefaultAssert;
import com.ttubeog.global.config.security.token.UserPrincipal;
import com.ttubeog.global.payload.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request){
        Long memberId = jwtTokenProvider.getMemberId(request);

        Optional<Member> checkUser = memberRepository.findById(memberId);
        DefaultAssert.isOptionalPresent(checkUser);
        Member member = checkUser.get();

        MemberDetailRes memberDetailRes = MemberDetailRes.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(memberDetailRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
