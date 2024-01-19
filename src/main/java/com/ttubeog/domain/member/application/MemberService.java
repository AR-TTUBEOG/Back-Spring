package com.ttubeog.domain.member.application;

import com.ttubeog.domain.member.dto.MemberDto;
import com.ttubeog.domain.member.dto.response.MemberDetailRes;
import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.global.DefaultAssert;
import com.ttubeog.global.config.security.token.UserPrincipal;
import com.ttubeog.global.payload.ApiResponse;
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

    // 현재 유저 조회
    public ResponseEntity<?> getCurrentUser(UserPrincipal userPrincipal){
        Optional<Member> checkUser = memberRepository.findById(userPrincipal.getId());
        DefaultAssert.isOptionalPresent(checkUser);
        Member member = checkUser.get();

        MemberDetailRes memberDetailRes = MemberDetailRes.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .ImgUrl(member.getImageUrl())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(memberDetailRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    public MemberDto findById(Long id) {
        Optional<Member> member = memberRepository.findById(id);

        return member.map(MemberDto::toEntity).orElse(null);
    }

    public MemberDto findByRefreshToken(String refreshToken) {
        Optional<Member> member = memberRepository.findByRefreshToken(refreshToken);
        return member.map(MemberDto::toEntity).orElse(null);
    }

    public void save(MemberDto memberDetailRes) {

    }

    public void update(MemberDto memberDetailRes) {

    }

    public void updateRefreshToken(MemberDto memberDetailRes) {
    }

}
