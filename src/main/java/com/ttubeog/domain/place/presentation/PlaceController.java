package com.ttubeog.domain.place.presentation;

import com.ttubeog.domain.place.application.PlaceService;
import com.ttubeog.domain.place.dto.response.GetAllPlaceRes;
import com.ttubeog.global.payload.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Place", description = "Place API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/place")
public class PlaceController {

    private final PlaceService placeService;

    // 전체 조회
    @Operation(summary = "전체 장소 조회", description = "전체 장소를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전체 장소 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetAllPlaceRes.class))}),
            @ApiResponse(responseCode = "400", description = "전체 장소 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping
    public ResponseEntity<?> getAllPlaces() {
        return placeService.getAllPlaces();
    }
}
