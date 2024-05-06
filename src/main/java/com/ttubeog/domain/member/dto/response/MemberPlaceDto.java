package com.ttubeog.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MemberPlaceDto {
    @Schema(description = "장소 ID", defaultValue = "1")
    private Long id;

    @Schema(description = "장소 이름", defaultValue = "나의 산책 스팟")
    private String name;

    @Schema(description = "세부 정보", defaultValue = "내 장소의 세부 정보")
    private String info;

    @Schema(description = "이미지 리스트")
    private String image;
}
