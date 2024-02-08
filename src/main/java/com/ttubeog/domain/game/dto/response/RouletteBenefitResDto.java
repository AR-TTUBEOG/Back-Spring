package com.ttubeog.domain.game.dto.response;

import com.ttubeog.domain.benefit.domain.BenefitType;
import lombok.Builder;
import lombok.Data;

@Data
public class RouletteBenefitResDto {

    private Long benefitId;

    private String content;

    private BenefitType type;

    @Builder
    public RouletteBenefitResDto(Long benefitId, String content, BenefitType type) {
        this.benefitId = benefitId;
        this.content = content;
        this.type = type;
    }
}
