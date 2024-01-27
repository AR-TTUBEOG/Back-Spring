package com.ttubeog.domain.game.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttubeog.domain.game.application.GameService;
import com.ttubeog.domain.game.dto.request.*;
import com.ttubeog.domain.game.dto.response.*;
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
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody CreateGiftReq createGiftReq
    ) throws JsonProcessingException {
        return gameService.createGift(userPrincipal, createGiftReq);
    }

    //농구 게임 생성
    @Operation(summary = "농구 게임 생성", description = "농구 게임을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "농구 게임 생성 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CreateBasketballRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "농구 게임 생성 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping("/basketball")
    public ResponseEntity<?> createBasketball(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody CreateBasketballReq createBasketballReq
            ) throws JsonProcessingException {
        return gameService.createBasketBall(userPrincipal, createBasketballReq);
    }

    //돌림판 게임 생성
    @Operation(summary = "돌림판 게임 생성", description = "돌림판 게임을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "돌림판 게임 생성 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CreateRouletteRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "돌림판 게임 생성 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping("/roulette")
    public ResponseEntity<?> createRoulette(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody CreateRouletteReq createRouletteReq
            ) throws JsonProcessingException {
        return gameService.createRoulette(userPrincipal, createRouletteReq);
    }

    //선물 게임 생성
    @Operation(summary = "선물 게임 수정", description = "선물 게임을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "선물 게임 수정 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateGiftRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "선물 게임 수정 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PatchMapping("/gift")
    public ResponseEntity<?> updateGift(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody UpdateGiftReq updateGiftReq
            ) throws JsonProcessingException {
        return gameService.updateGift(userPrincipal, updateGiftReq);
    }

    //농구 게임 생성
    @Operation(summary = "농구 게임 수정", description = "농구 게임을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "농구 게임 수정 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateBasketballRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "농구 게임 수정 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PatchMapping("/basketball")
    public ResponseEntity<?> updateBasketball(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody UpdateBasketballReq updateBasketballReq
            ) throws JsonProcessingException {
        return gameService.updateBasketball(userPrincipal, updateBasketballReq);
    }

}
