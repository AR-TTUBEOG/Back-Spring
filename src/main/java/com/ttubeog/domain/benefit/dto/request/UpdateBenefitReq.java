package com.ttubeog.domain.benefit.dto.request;

import com.ttubeog.domain.benefit.domain.BenefitType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "UpdateBenefitRequest")
public class UpdateBenefitReq {

    @Schema(description = "혜택 ID", example = "1")
    private Long benefitId;

    @Schema(description = "내용", example = "아메리카노 20% 할인")
    private String content;

}
