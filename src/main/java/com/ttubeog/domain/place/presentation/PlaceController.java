package com.ttubeog.domain.place.presentation;

import com.ttubeog.domain.place.application.PlaceService;
import com.ttubeog.domain.place.dto.request.GetNearbyPlaceReq;
import com.ttubeog.domain.place.dto.response.GetAllPlaceRes;
import com.ttubeog.global.payload.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> getAllPlaces(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return placeService.getAllPlaces(pageable);
    }

    // 추천순 조회
    @Operation(summary = "전체 장소 추천순 조회", description = "전체 장소를 추천순으로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추천순 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetAllPlaceRes.class))}),
            @ApiResponse(responseCode = "400", description = "추천순 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("/recommended")
    public ResponseEntity<?> getAllPlacesRecommended(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return placeService.getAllPlacesRecommended(pageable);
    }

    // 거리순 조회
    @Operation(summary = "전체 장소 거리순 조회", description = "전체 장소를 거리순으로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "거리순 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetAllPlaceRes.class))}),
            @ApiResponse(responseCode = "400", description = "거리순 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("/nearby")
    public ResponseEntity<?> getAllPlacesNearby(
            @Valid @RequestBody GetNearbyPlaceReq getNearbyPlaceReq,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return placeService.getAllPlacesNearby(getNearbyPlaceReq, pageable);
    }

    // 최신순 조회
    @Operation(summary = "전체 장소 최신순 조회", description = "전체 장소를 최신순으로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "최신순 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetAllPlaceRes.class))}),
            @ApiResponse(responseCode = "400", description = "최신순 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("/latest")
    public ResponseEntity<?> getAllPlacesLatest(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return placeService.getAllPlacesLatest(pageable);
    }
}
