package com.ttubeog.domain.benefit.dto.request;

import com.ttubeog.domain.benefit.domain.BenefitType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigInteger;

@Data
@Schema(description = "BenefitRequest")
public class CreateBenefitReq {

//    @Schema(description = "매장 ID", example = "1")
//    private Long storeId;

    @Schema(description = "내용", example = "아메리카노 20% 할인")
    private String content;

    @Schema(description = "혜택타입", example = "sale")
    private BenefitType type;

}
