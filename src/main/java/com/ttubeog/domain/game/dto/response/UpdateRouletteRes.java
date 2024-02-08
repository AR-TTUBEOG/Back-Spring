package com.ttubeog.domain.game.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class UpdateRouletteRes {

    @Schema(description = "게임 ID", example = "1")
    private Long gameId;

    @Schema(description = "옵션 내용", example = "[\"꽝\",\"5% 할인\",\"아메리카노 증정\",\"꽝\"]")
    private List<String> options;

    @Builder
    public UpdateRouletteRes(Long gameId, List<String> options) {
        this.gameId = gameId;
        this.options = options;
    }
}
