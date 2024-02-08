package com.ttubeog.domain.game.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
public class UpdateGiftRes {

    @Schema(description = "게임 ID", example = "1")
    private Long gameId;

    @Schema(description = "시간제한", example = "00:01:30")
    private LocalTime timeLimit;

    @Schema(description = "선물개수", example = "3")
    private Integer giftCount;

    @Builder
    public UpdateGiftRes(Long gameId, LocalTime timeLimit, Integer giftCount) {
        this.gameId = gameId;
        this.timeLimit = timeLimit;
        this.giftCount = giftCount;
    }
}
