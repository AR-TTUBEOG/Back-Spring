package com.ttubeog.domain.game.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalTime;

@Data
@Schema(description = "UpdateGiftGameRequest")
public class UpdateGiftReq {

    @Schema(description = "게임 ID", example = "1")
    private Long gameId;

    @Schema(description = "시간제한", example = "00:01:30")
    private LocalTime timeLimit;

    @Schema(description = "선물개수", example = "3")
    private Integer giftCount;
}
