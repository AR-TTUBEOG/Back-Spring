package com.ttubeog.domain.member.dto;

import com.ttubeog.domain.auth.domain.Platform;
import com.ttubeog.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MemberDto {
    private Long id;
    private String email;
    private String name;
    private String password;
    private Platform platform;
    private String refreshToken;

    public static MemberDto toEntity(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getNickname())
                .platform(member.getPlatform())
                .build();
    }
}
