package com.ttubeog.domain.game.dto.response;

import com.ttubeog.domain.game.domain.GameType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
public class FindGameRes {

    @Schema(description = "게임 ID", example = "1")
    private Long gameId;

    @Schema(description = "종류", example = "roulette")
    private GameType type;

    @Schema(description = "시간제한", example = "15")
    private Integer timeLimit;

    @Schema(description = "선물개수", example = "3")
    private Integer giftCount;

    @Schema(description = "공 개수", example = "10")
    private Integer ballCount;

    @Schema(description = "성공 개수", example = "4")
    private Integer successCount;

    @Schema(description = "옵션 내용", example = "[\"꽝\",\"5% 할인\",\"아메리카노 증정\",\"꽝\"]")
    private List<String> options;

    @Schema(description = "해당 혜택")
    private List<BenefitResDto> benefits;

    @Builder
    public FindGameRes(Long gameId, GameType type, Integer timeLimit, Integer giftCount, Integer ballCount, Integer successCount, List<String> options, List<BenefitResDto> benefits) {
        this.gameId = gameId;
        this.type = type;
        this.timeLimit = timeLimit;
        this.giftCount = giftCount;
        this.ballCount = ballCount;
        this.successCount = successCount;
        this.options = options;
        this.benefits = benefits;
    }
}
