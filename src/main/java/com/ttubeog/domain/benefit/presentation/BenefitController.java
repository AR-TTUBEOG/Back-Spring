package com.ttubeog.domain.benefit.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttubeog.domain.benefit.application.BenefitService;
import com.ttubeog.domain.benefit.dto.response.SaveBenefitRes;
import com.ttubeog.global.payload.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Benefit", description = "Benefit API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/benefit")
public class BenefitController {

    private final BenefitService benefitService;

    //게임 성공 후 혜택 저장
    @Operation(summary = "혜택 저장", description = "게임 성공 후 혜택을 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "혜택 저장 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = SaveBenefitRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "혜택 저장 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PatchMapping("/{benefitId}")
    public ResponseEntity<?> saveBenefit(
            HttpServletRequest request,
            @PathVariable(value = "benefitId") Long benefitId
    ) throws JsonProcessingException {
        return ResponseEntity.ok(benefitService.saveBenefit(request, benefitId));
    }

    //혜택 사용
    @Operation(summary = "내 혜택 사용", description = "멤버가 보유 중인 혜택을 사용합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "혜택 사용 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = SaveBenefitRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "혜택 사용 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PatchMapping("/{benefitId}/use")
    public ResponseEntity<?> useBenefit(
            HttpServletRequest request,
            @PathVariable(value = "benefitId") Long benefitId
    ) throws JsonProcessingException {
        return ResponseEntity.ok(benefitService.useBenefit(request, benefitId));
    }

    //혜택 조회
    @Operation(summary = "내 혜택 조회", description = "멤버가 보유 중인 혜택을 모두 조회합니다. 페이지 번호 0번이 첫 번째 페이지 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "혜택 조회 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = SaveBenefitRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "혜택 조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping
    public ResponseEntity<?> findMyBenefit(
            HttpServletRequest request,
            @RequestParam(name = "page") Integer page
    ) throws JsonProcessingException {
        return ResponseEntity.ok(benefitService.findMyBenefit(request, page));
    }
}
