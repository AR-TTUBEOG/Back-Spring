package com.ttubeog.domain.game.dto.request;

import com.ttubeog.domain.benefit.domain.BenefitType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class UpdateRouletteReq {

    @Schema(description = "게임 ID", example = "1")
    private Long gameId;

    @Schema(description = "매장ID", example = "1")
    private Long storeId;

    @Schema(description = "혜택 타입", example = "SALE")
    private BenefitType benefitType;

    @Schema(description = "옵션 내용", example = "[\"5% 할인\",\"아메리카노 증정\"]")
    private List<String> options;

}
