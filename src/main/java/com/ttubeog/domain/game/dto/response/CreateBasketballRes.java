package com.ttubeog.domain.game.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
public class CreateBasketballRes {

    @Schema(description = "게임 ID", example = "1")
    private Long gameId;

    @Schema(description = "혜택 ID", example = "1")
    private Long benefitId;

    @Schema(description = "시간제한", example = "00:01:30")
    private LocalTime timeLimit;

    @Schema(description = "공 개수", example = "10")
    private Integer ballCount;

    @Schema(description = "성공 개수", example = "4")
    private Integer successCount;

    @Builder
    public CreateBasketballRes(Long gameId, Long benefitId, LocalTime timeLimit, Integer ballCount, Integer successCount) {
        this.gameId = gameId;
        this.benefitId = benefitId;
        this.timeLimit = timeLimit;
        this.ballCount = ballCount;
        this.successCount = successCount;
    }
}
