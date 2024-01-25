package com.ttubeog.domain.member.dto;

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
    private String platform;
    private String refreshToken;

    public MemberDto(String subject, String email) {
    }

    public static MemberDto toEntity(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .platform(member.getPlatform())
                .build();
    }
}
