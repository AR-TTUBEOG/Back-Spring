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
public class MemberDetailDto {

    private Long id;

    private String name;

    private Platform platform;

    private Boolean isChanged;

    public static MemberDetailDto toDto(Member member) {
        return MemberDetailDto.builder()
                .id(member.getId())
                .name(member.getNickname())
                .platform(member.getPlatform())
                .build();
    }

}
