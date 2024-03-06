package com.ttubeog.domain.store.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttubeog.domain.game.application.GameService;
import com.ttubeog.domain.game.dto.response.FindGameRes;
import com.ttubeog.domain.likes.application.LikesService;
import com.ttubeog.domain.store.application.StoreService;
import com.ttubeog.domain.store.dto.request.RegisterStoreReq;
import com.ttubeog.domain.store.dto.request.UpdateStoreReq;
import com.ttubeog.domain.store.dto.response.GetStoreDetailRes;
import com.ttubeog.domain.store.dto.response.RegisterStoreRes;
import com.ttubeog.domain.store.dto.response.UpdateStoreRes;
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
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Store", description = "Store API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store")
public class StoreController {

    private final StoreService storeService;
    private final LikesService likesService;
    private final GameService gameService;


    /**
     * 매장 등록 API
     * @param request 유저 검증
     * @param registerStoreReq 매장 등록 DTO
     * @return ApiResponse (check: true, information: registerStoreRes)
     */
    @Operation(summary = "매장 등록", description = "매장을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RegisterStoreRes.class))}),
            @ApiResponse(responseCode = "400", description = "매장 등록 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping
    public ResponseEntity<?> registerStore(
            HttpServletRequest request,
            @Valid @RequestBody RegisterStoreReq registerStoreReq
    ) {
        return storeService.registerStore(request, registerStoreReq);
    }

    /**
     * 매장 수정 API
     * @param request 유저 검증
     * @param updateStoreReq 매장 수정 DTO
     * @return ApiResponse (check: true, information: updateStoreRes)
     */
    @Operation(summary = "매장 수정", description = "매장 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UpdateStoreRes.class))}),
            @ApiResponse(responseCode = "400", description = "매장 수정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PatchMapping
    public ResponseEntity<?> updateStore(
            HttpServletRequest request,
            @Valid @RequestBody UpdateStoreReq updateStoreReq
    ) {
        return storeService.updateStore(request, updateStoreReq);
    }

    /**
     * 매장 삭제 API
     * @param request 유저 검증
     * @param storeId 매장 ID
     * @return ApiResponse (check: true, information: message)
     */
    @Operation(summary = "매장 삭제", description = "매장 정보를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "매장 삭제 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @DeleteMapping("/{storeId}")
    public ResponseEntity<?> deleteStore(
            HttpServletRequest request,
            @PathVariable Long storeId
    ) {
        return storeService.deleteStore(request, storeId);
    }

    /**
     * 매장 세부사항 조회 API
     * @param request 유저검증
     * @param storeId 매장 ID
     * @return ApiResponse (check: true, information: getStoreDetailRes)
     */
    @Operation(summary = "매장 세부사항 조회", description = "매장 세부사항을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매장 ", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetStoreDetailRes.class))}),
            @ApiResponse(responseCode = "400", description = "매장 세부사항 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("/{storeId}")
    public ResponseEntity<?> getStoreDetails(
            HttpServletRequest request,
            @PathVariable Long storeId
    ) {
        return storeService.getStoreDetails(request, storeId);
    }

    /**
     * 매장 좋아요 누르기 API
     * @param request 유저 검증
     * @param storeId 매장 ID
     * @return ApiResponse (check: true, information: message)
     */
    @Operation(summary = "매장 좋아요 누르기", description = "매장에 대한 좋아요를 누릅니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매장 좋아요 누르기 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "매장 좋아요 누르기 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping("/{storeId}/likes")
    public ResponseEntity<?> likesStore(
            HttpServletRequest request,
            @PathVariable Long storeId
    ) {
        return likesService.likesStore(request, storeId);
    }

    /**
     * 매장 혜택 조회 API
     * @param request 유저 검증
     * @param storeId 매장 ID
     * @return ApiResponse (check: true, information: findGameRes)
     * @throws JsonProcessingException JSON Processing 에러
     */
    @Operation(summary = "매장으로 게임, 혜택 조회", description = "매장ID에 해당하는 모든 게임과 해당 혜택을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게임 조회 누르기 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FindGameRes.class))}),
            @ApiResponse(responseCode = "400", description = "게임 조회 누르기 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("/{storeId}/game")
    public ResponseEntity<?> findBenefitByStore(
            HttpServletRequest request,
            @PathVariable Long storeId
    ) throws JsonProcessingException {
        return gameService.findByStore(request, storeId);
    }
}
