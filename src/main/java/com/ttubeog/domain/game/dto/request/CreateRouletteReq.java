package com.ttubeog.domain.game.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "CreateRouletteGameRequest")
public class CreateRouletteReq {

    @Schema(description = "혜택 ID", example = "1")
    private Long benefitId;

    @Schema(description = "옵션 수", example = "4")
    private Integer option;

    @Schema(description = "옵션 내용", example = "[\"꽝\",\"5% 할인\",\"아메리카노 증정\",\"꽝\"]")
    private String[] contents;
}
