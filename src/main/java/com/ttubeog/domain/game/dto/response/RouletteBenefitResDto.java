package com.ttubeog.domain.game.dto.response;

import com.ttubeog.domain.benefit.domain.BenefitType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
public class RouletteBenefitResDto {


    @Schema(description = "혜택 Id", example = "1")
    private Long benefitId;


    @Schema(description = "혜택 내용", example = "아메리카노 20% 할인")
    private String content;


    @Schema(description = "혜택 타입", example = "SALE")
    private BenefitType type;

    @Builder
    public RouletteBenefitResDto(Long benefitId, String content, BenefitType type) {
        this.benefitId = benefitId;
        this.content = content;
        this.type = type;
    }
}
