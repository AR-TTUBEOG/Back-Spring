package com.ttubeog.domain.game.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttubeog.domain.game.application.GameService;
import com.ttubeog.domain.game.dto.request.*;
import com.ttubeog.domain.game.dto.response.*;
import com.ttubeog.global.payload.ErrorResponse;
import com.ttubeog.global.payload.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Game", description = "Game API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/game")
public class GameController {

    private final GameService gameService;

    //선물 게임 생성
    @Operation(summary = "선물 게임 생성", description = "선물 게임을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "선물 게임 생성 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CreateGiftRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "선물 게임 생성 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping("/gift")
    public ResponseEntity<?> createGift(
            HttpServletRequest request,
            @Valid @RequestBody CreateGiftReq createGiftReq
    ) throws JsonProcessingException {
        return gameService.createGift(request, createGiftReq);
    }

    //농구 게임 생성
    @Operation(summary = "농구 게임 생성", description = "농구 게임을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "농구 게임 생성 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CreateBasketballRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "농구 게임 생성 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping("/basketball")
    public ResponseEntity<?> createBasketball(
            HttpServletRequest request,
            @Valid @RequestBody CreateBasketballReq createBasketballReq
            ) throws JsonProcessingException {
        return gameService.createBasketBall(request, createBasketballReq);
    }

    //돌림판 게임 생성
    @Operation(summary = "돌림판 게임 생성", description = "돌림판 게임을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "돌림판 게임 생성 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CreateRouletteRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "돌림판 게임 생성 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping("/roulette")
    public ResponseEntity<?> createRoulette(
            HttpServletRequest request,
            @Valid @RequestBody CreateRouletteReq createRouletteReq
            ) throws JsonProcessingException {
        return gameService.createRoulette(request, createRouletteReq);
    }

    //선물 게임 수정
    @Operation(summary = "선물 게임 수정", description = "선물 게임을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "선물 게임 수정 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateGiftRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "선물 게임 수정 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PatchMapping("/gift")
    public ResponseEntity<?> updateGift(
            HttpServletRequest request,
            @Valid @RequestBody UpdateGiftReq updateGiftReq
            ) throws JsonProcessingException {
        return gameService.updateGift(request, updateGiftReq);
    }

    //농구 게임 수정
    @Operation(summary = "농구 게임 수정", description = "농구 게임을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "농구 게임 수정 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateBasketballRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "농구 게임 수정 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PatchMapping("/basketball")
    public ResponseEntity<?> updateBasketball(
            HttpServletRequest request,
            @Valid @RequestBody UpdateBasketballReq updateBasketballReq
            ) throws JsonProcessingException {
        return gameService.updateBasketball(request, updateBasketballReq);
    }

    //돌림판 게임 수정
    @Operation(summary = "돌림판 게임 수정", description = "돌림판 게임을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "돌림판 게임 수정 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateRouletteRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "돌림판 게임 수정 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PatchMapping("/roulette")
    public ResponseEntity<?> updateRoulette(
            HttpServletRequest request,
            @Valid @RequestBody UpdateRouletteReq updateRouletteReq
    ) throws JsonProcessingException {
        return gameService.updateRoulette(request, updateRouletteReq);
    }

    //게임 삭제
    @Operation(summary = "게임 삭제", description = "게임을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
            @ApiResponse(responseCode = "400", description = "삭제 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @DeleteMapping("/{gameId}")
    public ResponseEntity<?> deleteGame(
            HttpServletRequest request,
            @PathVariable(value = "gameId") Long gameId
    ) throws JsonProcessingException {
        return gameService.deleteGame(request, gameId);
    }

    //게임, 혜택 조회
    @Operation(summary = "게임,혜택 조회", description = "게임과 해당 혜택을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = FindGameRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping("/{gameId}")
    public ResponseEntity<?> findGame(
            HttpServletRequest request,
            @PathVariable(value = "gameId") Long gameId
    ) throws JsonProcessingException {
        return gameService.findGame(request, gameId);
    }

    //게임 조회
    @Operation(summary = "혜택 조회", description = "게임Id로 혜택 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = BenefitResDto.class) ) } ),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping("/{gameId}/benefit")
    public ResponseEntity<?> findBenefit(
            HttpServletRequest request,
            @PathVariable(value = "gameId") Long gameId
    ) throws JsonProcessingException {
        return gameService.findBenefit(request, gameId);
    }

}
