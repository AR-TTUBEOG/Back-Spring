package com.ttubeog.domain.member.dto.response;

import com.ttubeog.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class MemberNicknameDto {

    private Long id;
    private String nickname;

    private Integer nicknameChanged;

    public static MemberNicknameDto toDto(Member member) {
        return MemberNicknameDto.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .nicknameChanged(member.getNicknameChange())
                .build();
    }

}
