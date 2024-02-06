package com.ttubeog.domain.game.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalTime;

@Data
@Schema(description = "UpdateBasketballGameRequest")
public class UpdateBasketballReq {

    @Schema(description = "게임 ID", example = "1")
    private Long gameId;

    @Schema(description = "시간제한", example = "00:01:30")
    private LocalTime timeLimit;

    @Schema(description = "공 개수", example = "10")
    private Integer ballCount;

    @Schema(description = "성공 개수", example = "4")
    private Integer successCount;
}
