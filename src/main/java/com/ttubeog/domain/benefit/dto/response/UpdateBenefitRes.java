package com.ttubeog.domain.benefit.dto.response;

import com.ttubeog.domain.benefit.domain.BenefitType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
public class UpdateBenefitRes {

    @Schema(description = "혜택 ID", example = "1")
    private Long benefitId;

//    @Schema(description = "매장 ID", example = "1")
//    private Long storeId;

    @Schema(description = "내용", example = "아메리카노 20% 할인")
    private String content;

    @Schema(description = "혜택타입", example = "sale")
    private BenefitType type;

    @Builder
//    public CreateBenefitRes(Long benefitId, Long storeId, String content, BenefitType type) {
    public UpdateBenefitRes(Long benefitId, String content, BenefitType type) {
        this.benefitId = benefitId;
//        this.storeId = storeId;
        this.content = content;
        this.type = type;
    }

}
