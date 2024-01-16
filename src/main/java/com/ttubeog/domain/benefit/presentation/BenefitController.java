package com.ttubeog.domain.benefit.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttubeog.domain.benefit.application.BenefitService;
import com.ttubeog.domain.benefit.dto.request.CreateBenefitReq;
import com.ttubeog.domain.benefit.dto.response.CreateBenefitRes;
import com.ttubeog.global.config.security.token.CurrentUser;
import com.ttubeog.global.config.security.token.UserPrincipal;
import com.ttubeog.global.payload.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Benefit", description = "Benefit API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/benefit")
public class BenefitController {

    private final BenefitService benefitService;

    //혜택 생성
    @Operation(summary = "혜택 생성", description = "매장의 혜택을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "혜택 생성 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CreateBenefitRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "혜택 생성 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping
    public ResponseEntity<?> createBenefit(
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true)
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody CreateBenefitReq createBenefitReq
    ) throws JsonProcessingException {
        return benefitService.createBenefit(userPrincipal, createBenefitReq);
    }
}
