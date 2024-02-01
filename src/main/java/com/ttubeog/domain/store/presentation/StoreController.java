package com.ttubeog.domain.store.presentation;

import com.ttubeog.domain.likes.application.LikesService;
import com.ttubeog.domain.store.application.StoreService;
import com.ttubeog.domain.store.dto.request.RegisterStoreReq;
import com.ttubeog.domain.store.dto.request.UpdateStoreReq;
import com.ttubeog.domain.store.dto.response.GetStoreDetailRes;
import com.ttubeog.domain.store.dto.response.RegisterStoreRes;
import com.ttubeog.domain.store.dto.response.UpdateStoreRes;
import com.ttubeog.global.config.security.token.CurrentUser;
import com.ttubeog.global.config.security.token.UserPrincipal;
import com.ttubeog.global.payload.Message;
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
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Store", description = "Store API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store")
public class StoreController {

    private final StoreService storeService;
    private final LikesService likesService;

    // 매장 등록
    @Operation(summary = "매장 등록", description = "매장을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매장 등록 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RegisterStoreRes.class))}),
            @ApiResponse(responseCode = "400", description = "매장 등록 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping
    public ResponseEntity<?> registerStore(
            @Parameter(description = "AccessToken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody RegisterStoreReq registerStoreReq
    ) {
        return storeService.registerStore(userPrincipal, registerStoreReq);
    }

    // 매장 수정
    @Operation(summary = "매장 수정", description = "매장 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매장 수정 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UpdateStoreRes.class))}),
            @ApiResponse(responseCode = "400", description = "매장 수정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PatchMapping
    public ResponseEntity<?> updateStore(
            @Parameter(description = "AccessToken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody UpdateStoreReq updateStoreReq
    ) {
        return storeService.updateStore(userPrincipal, updateStoreReq);
    }

    // 매장 삭제
    @Operation(summary = "매장 삭제", description = "매장 정보를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매장 삭제 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "매장 삭제 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @DeleteMapping("/{storeId}")
    public ResponseEntity<?> deleteStore(
            @Parameter(description = "AccessToken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @PathVariable Long storeId
    ) {
        return storeService.deleteStore(userPrincipal, storeId);
    }

    // 매장 세부사항 조회
    @Operation(summary = "매장 세부사항 조회", description = "매장 세부사항을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매장 세부사항 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetStoreDetailRes.class))}),
            @ApiResponse(responseCode = "400", description = "매장 세부사항 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("/{storeId}")
    public ResponseEntity<?> getStoreDetails(
            @PathVariable Long storeId
    ) {
        return storeService.getStoreDetails(storeId);
    }

    // 매장 좋아요 누르기
    @Operation(summary = "매장 좋아요 누르기", description = "매장에 대한 좋아요를 누릅니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매장 좋아요 누르기 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "매장 좋아요 누르기 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping("/{storeId}/likes")
    public ResponseEntity<?> likesStore(
            @Parameter(description = "AccessToken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @PathVariable Long storeId
    ) {
        return likesService.likesStore(userPrincipal, storeId);
    }
}
