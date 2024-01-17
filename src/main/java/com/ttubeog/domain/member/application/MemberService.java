package com.ttubeog.domain.member.application;

import com.ttubeog.domain.member.dto.MemberDto;
import com.ttubeog.domain.member.dto.response.MemberDetailRes;
import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.global.DefaultAssert;
import com.ttubeog.global.config.security.token.MemberPrincipal;
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
    public ResponseEntity<?> getCurrentUser(MemberPrincipal memberPrincipal){
        Optional<Member> checkUser = memberRepository.findById(memberPrincipal.getId());
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
        Optional<Member> optionalMember = memberRepository.findById(id);

        return optionalMember.map(MemberDto::toEntity).orElse(null);
    }


    public void save(MemberDto memberDetailRes) {

    }

    public void update(MemberDto memberDetailRes) {

    }

    public void updateRefreshToken(MemberDto memberDetailRes) {
    }

}
