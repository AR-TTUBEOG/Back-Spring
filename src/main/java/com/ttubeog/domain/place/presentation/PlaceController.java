package com.ttubeog.domain.place.presentation;

import com.ttubeog.domain.place.application.PlaceService;
import com.ttubeog.domain.place.dto.request.GetNearbyPlaceReq;
import com.ttubeog.domain.place.dto.request.SearchPlaceReq;
import com.ttubeog.domain.place.dto.response.GetAllPlaceRes;
import com.ttubeog.domain.place.dto.response.SearchPlaceRes;
import com.ttubeog.global.payload.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<?> getAllPlaces(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return placeService.getAllPlaces(request, pageable);
    }

    // 추천순 조회
    @Operation(summary = "전체 장소 추천순 조회", description = "전체 장소를 추천순으로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추천순 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetAllPlaceRes.class))}),
            @ApiResponse(responseCode = "400", description = "추천순 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("/recommended")
    public ResponseEntity<?> getAllPlacesRecommended(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return placeService.getAllPlacesRecommended(request, pageable);
    }

    // 거리순 조회
    @Operation(summary = "전체 장소 거리순 조회", description = "전체 장소를 거리순으로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "거리순 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetAllPlaceRes.class))}),
            @ApiResponse(responseCode = "400", description = "거리순 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("/nearby")
    public ResponseEntity<?> getAllPlacesNearby(
            HttpServletRequest request,
            @Valid GetNearbyPlaceReq getNearbyPlaceReq,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return placeService.getAllPlacesNearby(request, getNearbyPlaceReq, pageable);
    }

    // 최신순 조회
    @Operation(summary = "전체 장소 최신순 조회", description = "전체 장소를 최신순으로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "최신순 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetAllPlaceRes.class))}),
            @ApiResponse(responseCode = "400", description = "최신순 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("/latest")
    public ResponseEntity<?> getAllPlacesLatest(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return placeService.getAllPlacesLatest(request, pageable);
    }

    // 장소 검색
    @Operation(summary = "장소 검색", description = "키워드를 입력받아 장소를 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "장소 검색 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SearchPlaceRes.class))}),
            @ApiResponse(responseCode = "400", description = "장소 검색 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("/search")
    public ResponseEntity<?> searchPlace(
            HttpServletRequest request,
            @Valid SearchPlaceReq searchPlaceReq,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return placeService.searchPlaces(request, searchPlaceReq, pageable);
    }
}
