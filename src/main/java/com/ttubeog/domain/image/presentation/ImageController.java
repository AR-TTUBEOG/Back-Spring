package com.ttubeog.domain.image.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttubeog.domain.benefit.dto.response.SaveBenefitRes;
import com.ttubeog.domain.image.application.ImageService;
import com.ttubeog.domain.image.dto.response.ImageResponseDto;
import com.ttubeog.global.config.security.token.CurrentUser;
import com.ttubeog.global.payload.CommonDto;
import com.ttubeog.global.payload.ErrorResponse;
import com.ttubeog.global.payload.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Image", description = "Image API(이미지 API)")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/image")
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "산책스팟 이미지 저장", description = "산책스팟의 이미지를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 저장 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ImageResponseDto.class) ) } ),
            @ApiResponse(responseCode = "400", description = "이미지 저장 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
//    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value = "/spot", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonDto> createSpotImage(
            @CurrentUser HttpServletRequest request,
            @RequestParam Long spotId,
            @RequestPart List<MultipartFile> fileList
    ) {
        return ResponseEntity.ok(imageService.createSpotImage(request, spotId, fileList));
    }

    @Operation(summary = "매장 이미지 저장", description = "매장의 이미지를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 저장 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ImageResponseDto.class) ) } ),
            @ApiResponse(responseCode = "400", description = "이미지 저장 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping(value = "/store", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<CommonDto> createStoreImage(
            @CurrentUser HttpServletRequest request,
            @RequestParam Long storeId,
            @RequestPart List<MultipartFile> fileList
    ) {
        return ResponseEntity.ok(imageService.createStoreImage(request, storeId, fileList));
    }

    @Operation(summary = "방명록 이미지 저장", description = "방명록의 이미지를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 저장 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ImageResponseDto.class) ) } ),
            @ApiResponse(responseCode = "400", description = "이미지 저장 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping(value = "/guestbook", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<CommonDto> createGuestBookImage(
            @CurrentUser HttpServletRequest request,
            @RequestParam Long guestBookId,
            @RequestPart List<MultipartFile> fileList
    ) {
        return ResponseEntity.ok(imageService.createGuestBookImage(request, guestBookId, fileList));
    }

    @Operation(summary = "산책스팟 이미지 조회", description = "산책스팟ID로 해당 이미지를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 조회 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ImageResponseDto.class) ) } ),
            @ApiResponse(responseCode = "400", description = "이미지 조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping(value = "/spot/{spotId}")
//    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<CommonDto> findImageBySpotId(
            @CurrentUser HttpServletRequest request,
            @PathVariable Long spotId
    ) {
        return ResponseEntity.ok(imageService.findImageBySpotId(request, spotId));
    }

    @Operation(summary = "매장 이미지 조회", description = "매장ID로 해당 이미지를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 조회 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ImageResponseDto.class) ) } ),
            @ApiResponse(responseCode = "400", description = "이미지 조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping(value = "/store/{storeId}")
//    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<CommonDto> findImageByStoreId(
            @CurrentUser HttpServletRequest request,
            @PathVariable Long storeId
    ) {
        return ResponseEntity.ok(imageService.findImageByStoreId(request, storeId));
    }

    @Operation(summary = "방명록 이미지 조회", description = "방명록ID로 해당 이미지를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 조회 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ImageResponseDto.class) ) } ),
            @ApiResponse(responseCode = "400", description = "이미지 조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping(value = "/guestbook/{guestBookId}")
//    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<CommonDto> findImageByGuestBookId(
            @CurrentUser HttpServletRequest request,
            @RequestParam Long guestBookId
    ) {
        return ResponseEntity.ok(imageService.findImageByGuestBookId(request, guestBookId));
    }


    @Operation(summary = "산책스팟 이미지 삭제", description = "산책스팟의 이미지를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 삭제 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
            @ApiResponse(responseCode = "400", description = "이미지 삭제 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @DeleteMapping(value = "/spot/{spotId}")
//    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<CommonDto> deleteImageBySpotId(
            @RequestParam Long spotId
    ) {
        return ResponseEntity.ok(imageService.deleteImageBySpotId(spotId));
    }

    @Operation(summary = "매장 이미지 삭제", description = "매장의 이미지를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 삭제 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
            @ApiResponse(responseCode = "400", description = "이미지 삭제 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @DeleteMapping(value = "/store/{storeId}")
//    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<CommonDto> deleteImageByStoreId(
            @RequestParam Long storeId
    ) {
        return ResponseEntity.ok(imageService.deleteImageByStoreId(storeId));
    }

    @Operation(summary = "방명록 이미지 삭제", description = "방명록의 이미지를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 삭제 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
            @ApiResponse(responseCode = "400", description = "이미지 삭제 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @DeleteMapping(value = "/guestbook/{guestBookId}")
//    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<CommonDto> deleteImageByGuestBookId(
            @RequestParam Long guestBookId
    ) {
        return ResponseEntity.ok(imageService.deleteImageByGuestBookId(guestBookId));
    }
}
