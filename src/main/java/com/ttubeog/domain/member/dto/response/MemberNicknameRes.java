package com.ttubeog.domain.member.dto.response;

import com.ttubeog.domain.auth.domain.Platform;
import com.ttubeog.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class MemberNicknameRes {

    private Long id;
    private String nickname;

    private Boolean nicknameChanged;

    public static MemberNicknameRes toDto(Member member) {
        return MemberNicknameRes.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .nicknameChanged(member.getNicknameChange())
                .build();
    }

}
