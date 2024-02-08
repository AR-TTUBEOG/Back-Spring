package com.ttubeog.domain.benefit.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttubeog.domain.benefit.application.BenefitService;
import com.ttubeog.domain.benefit.dto.request.CreateBenefitReq;
import com.ttubeog.domain.benefit.dto.request.UpdateBenefitReq;
import com.ttubeog.domain.benefit.dto.response.CreateBenefitRes;
import com.ttubeog.domain.benefit.dto.response.SaveBenefitRes;
import com.ttubeog.domain.benefit.dto.response.UpdateBenefitRes;
import com.ttubeog.domain.game.dto.response.FindGameRes;
import com.ttubeog.global.config.security.token.CurrentUser;
import com.ttubeog.global.config.security.token.UserPrincipal;
import com.ttubeog.global.payload.ErrorResponse;
import com.ttubeog.global.payload.Message;
import io.swagger.v3.oas.annotations.Operation;
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

//    //혜택 생성
//    @Operation(summary = "혜택 생성", description = "매장의 혜택을 생성합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "혜택 생성 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CreateBenefitRes.class) ) } ),
//            @ApiResponse(responseCode = "400", description = "혜택 생성 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
//    })
//    @PostMapping
//    public ResponseEntity<?> createBenefit(
//            @CurrentUser UserPrincipal userPrincipal,
//            @Valid @RequestBody CreateBenefitReq createBenefitReq
//    ) throws JsonProcessingException {
//        return benefitService.createBenefit(userPrincipal, createBenefitReq);
//    }

//    //혜택 삭제
//    @Operation(summary = "혜택 삭제", description = "매장의 혜택을 삭제합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "혜택 삭제 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
//            @ApiResponse(responseCode = "400", description = "혜택 삭제 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
//    })
//    @DeleteMapping("/{benefitId}")
//    public ResponseEntity<?> deleteBenefit(
//            @CurrentUser UserPrincipal userPrincipal,
//            @PathVariable(value = "benefitId") Long benefitId
//    ) throws JsonProcessingException {
//        return benefitService.deleteBenefit(userPrincipal, benefitId);
//    }

    //혜택 수정
//    @Operation(summary = "혜택 수정", description = "매장의 혜택을 수정합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "혜택 수정 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateBenefitRes.class) ) } ),
//            @ApiResponse(responseCode = "400", description = "혜택 수정 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
//    })
//    @PatchMapping
//    public ResponseEntity<?> updateBenefit(
//            @CurrentUser UserPrincipal userPrincipal,
//            @Valid @RequestBody UpdateBenefitReq updateBenefitReq
//            ) throws JsonProcessingException {
//        return benefitService.updateBenefit(userPrincipal, updateBenefitReq);
//    }

    //게임 성공 후 혜택 저장
    @Operation(summary = "혜택 저장", description = "게임 성공 후 혜택을 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "혜택 저장 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = SaveBenefitRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "혜택 저장 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PatchMapping("/{benefitId}")
    public ResponseEntity<?> saveBenefit(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(value = "benefitId") Long benefitId
    ) throws JsonProcessingException {
        return benefitService.saveBenefit(userPrincipal, benefitId);
    }

    //혜택 사용
    @Operation(summary = "내 혜택 사용", description = "멤버가 보유 중인 혜택을 사용합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "혜택 사용 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = SaveBenefitRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "혜택 사용 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PatchMapping("{benefitId}/use")
    public ResponseEntity<?> useBenefit(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(value = "benefitId") Long benefitId
    ) throws JsonProcessingException {
        return benefitService.useBenefit(userPrincipal, benefitId);
    }

    //혜택 조회
    @Operation(summary = "내 혜택 조회", description = "멤버가 보유 중인 혜택을 모두 조회합니다. 페이지 번호 0번이 첫 번째 페이지 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "혜택 조회 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = SaveBenefitRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "혜택 조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping
    public ResponseEntity<?> findMyBenefit(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "page") Integer page
    ) throws JsonProcessingException {
        return benefitService.findMyBenefit(userPrincipal, page);
    }

//    //혜택으로 모든 게임 조회
//    @Operation(summary = "혜택으로 게임 조회", description = "혜택에 해당하는 게임을 모두 조회합니다. 최대 3개입니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "게임 조회 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = FindGameRes.class) ) } ),
//            @ApiResponse(responseCode = "400", description = "게임 조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
//    })
//    @GetMapping("/{benefitId}/game")
//    public ResponseEntity<?> findGames(
//            @CurrentUser UserPrincipal userPrincipal,
//            @PathVariable(value = "benefitId") Long benefitId
//    ) throws JsonProcessingException {
//        return benefitService.findGames(userPrincipal, benefitId);
//    }
}
