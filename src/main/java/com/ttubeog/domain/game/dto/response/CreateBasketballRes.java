package com.ttubeog.domain.game.dto.response;

import com.ttubeog.domain.benefit.domain.BenefitType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
public class CreateBasketballRes {

    @Schema(description = "게임 ID", example = "1")
    private Long gameId;

    @Schema(description = "혜택 ID", example = "1")
    private Long benefitId;

    @Schema(description = "매장ID", example = "1")
    private Long storeId;

    @Schema(description = "시간제한", example = "15")
    private Integer timeLimit;

    @Schema(description = "공 개수", example = "10")
    private Integer ballCount;

    @Schema(description = "성공 개수", example = "4")
    private Integer successCount;

    @Schema(description = "혜택 내용", example = "아메리카노 20% 할인")
    private String benefitContent;

    @Schema(description = "혜택 종류", example = "SALE")
    private BenefitType benefitType;

    @Builder
    public CreateBasketballRes(Long gameId, Long benefitId, Long storeId, Integer timeLimit, Integer ballCount, Integer successCount, String benefitContent, BenefitType benefitType) {
        this.gameId = gameId;
        this.benefitId = benefitId;
        this.storeId = storeId;
        this.timeLimit = timeLimit;
        this.ballCount = ballCount;
        this.successCount = successCount;
        this.benefitContent = benefitContent;
        this.benefitType = benefitType;
    }
}
