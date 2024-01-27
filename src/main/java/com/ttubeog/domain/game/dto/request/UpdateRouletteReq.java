package com.ttubeog.domain.game.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class UpdateRouletteReq {

    @Schema(description = "게임 ID", example = "1")
    private Long gameId;

    @Schema(description = "옵션 내용", example = "[\"꽝\",\"5% 할인\",\"아메리카노 증정\",\"꽝\"]")
    private List<String> options;

}
