package com.ttubeog.domain.store.presentation;

import com.ttubeog.domain.store.application.StoreService;
import com.ttubeog.domain.store.dto.request.RegisterStoreReq;
import com.ttubeog.domain.store.dto.response.RegisterStoreRes;
import com.ttubeog.global.config.security.token.CurrentUser;
import com.ttubeog.global.config.security.token.UserPrincipal;
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

}
