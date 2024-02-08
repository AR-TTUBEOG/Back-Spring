package com.ttubeog.domain.spot.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttubeog.domain.guestbook.application.GuestBookService;
import com.ttubeog.domain.guestbook.dto.CreateGuestBookRequestDto;
import com.ttubeog.domain.spot.application.SpotService;
import com.ttubeog.domain.spot.dto.request.CreateSpotRequestDto;
import com.ttubeog.domain.spot.dto.request.UpdateSpotRequestDto;
import com.ttubeog.global.config.security.token.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Spot", description = "Spot API(산책 스팟 API)")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/spot")
public class SpotController {

    private final SpotService spotService;
    private final GuestBookService guestBookService;

    @Operation(summary = "산책 스팟 생성", description = "산책 스팟을 생성합니다.")
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<?> createSpot(
            @CurrentUser HttpServletRequest request,
            @RequestBody CreateSpotRequestDto createSpotRequestDto
    ) throws JsonProcessingException {
        return spotService.createSpot(request, createSpotRequestDto);
    }

    @Operation(summary = "산책 스팟 세부 사항 조회", description = "산책 스팟의 세부 사항을 조회합니다.")
    @GetMapping("/{spotId}")
    public ResponseEntity<?> findBySpotId(
            @CurrentUser HttpServletRequest request,
            @RequestParam(name = "spotId") Long spotId
    ) throws JsonProcessingException {
        return spotService.findBySpotId(request, spotId);
    }

    @Operation(summary = "산책 스팟 수정", description = "산책 스팟에 대한 정보를 수정합니다.")
    @PatchMapping
    public ResponseEntity<?> updateSpot(
            @CurrentUser HttpServletRequest request,
            @RequestParam(name = "spotId") Long spotId,
            @RequestBody UpdateSpotRequestDto updateSpotRequestDto
    ) throws JsonProcessingException {
        return spotService.updateSpot(request, spotId, updateSpotRequestDto);
    }

    @Operation(summary = "산책 스팟 삭제", description = "산책 스팟을 삭제합니다.")
    @DeleteMapping("/{spotId}")
    public ResponseEntity<?> deleteSpot(
            @CurrentUser HttpServletRequest request,
            @RequestParam(name = "spotId") Long spotId
    ) throws JsonProcessingException {
        return spotService.deleteSpot(request, spotId);
    }

    @Operation(summary = "산책 스팟 방명록 작성", description = "산책 스팟에 종속된 방명록을 작성합니다.")
    @PostMapping("/{spotId}/guestbook")
    public ResponseEntity<?> createGuestBook(
            @CurrentUser HttpServletRequest request,
            @RequestParam(name = "spotId") Integer spotId,
            @RequestBody CreateGuestBookRequestDto createGuestBookRequestDto
    ) throws JsonProcessingException {
        return guestBookService.createGuestBook(request, spotId, createGuestBookRequestDto);
    }

    @Operation(summary = "산책 스팟 좋아요 누르기", description = "산책 스팟에 좋아요를 누릅니다.")
    @PatchMapping("/{spotId}/likes")
    public ResponseEntity<?> likeSpot(
            @CurrentUser HttpServletRequest request,
            @RequestParam(name = "spotId") Integer spotId
    ) throws JsonProcessingException {
        return spotService.likeSpot(request, spotId);
    }
}
