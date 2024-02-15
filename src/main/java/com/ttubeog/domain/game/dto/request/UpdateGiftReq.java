package com.ttubeog.domain.game.dto.request;

import com.ttubeog.domain.benefit.domain.BenefitType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "UpdateGiftGameRequest")
public class UpdateGiftReq {

    @Schema(description = "게임 ID", example = "1")
    private Long gameId;

    @Schema(description = "매장ID", example = "1")
    private Long storeId;

    @Schema(description = "시간제한", example = "15")
    private Integer timeLimit;

    @Schema(description = "선물개수", example = "3")
    private Integer giftCount;

    @Schema(description = "혜택 내용", example = "아메리카노 20% 할인")
    private String benefitContent;

    @Schema(description = "혜택 종류", example = "SALE")
    private BenefitType benefitType;
}
