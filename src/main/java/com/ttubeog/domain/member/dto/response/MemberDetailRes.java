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
public class MemberDetailRes {

    private Long id;

    private String name;

    private Platform platform;

    private Boolean isUsed;

    public static MemberDetailRes toDto(Member member) {
        return MemberDetailRes.builder()
                .id(member.getId())
                .name(member.getNickname())
                .platform(member.getPlatform())
                .build();
    }

}
