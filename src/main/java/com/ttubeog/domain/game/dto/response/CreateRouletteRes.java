package com.ttubeog.domain.game.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class CreateRouletteRes {

    @Schema(description = "게임 ID", example = "1")
    private Long gameId;

    @Schema(description = "혜택 ID", example = "1")
    private Long benefitId;

    @Schema(description = "옵션 개수", example = "4")
    private Integer option;

    @Schema(description = "옵션 내용", example = "[\"꽝\",\"5% 할인\",\"아메리카노 증정\",\"꽝\"]")
    private List<String> contents;

    @Builder
    public CreateRouletteRes(Long gameId, Long benefitId, Integer option, List<String> contents) {
        this.gameId = gameId;
        this.benefitId = benefitId;
        this.option = option;
        this.contents = contents;
    }
}
