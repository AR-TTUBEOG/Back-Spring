package com.ttubeog.domain.spot.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttubeog.domain.guestbook.application.GuestBookService;
import com.ttubeog.domain.guestbook.dto.request.CreateGuestBookRequestDto;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import com.ttubeog.domain.spot.application.SpotService;
import com.ttubeog.domain.spot.dto.request.CreateSpotRequestDto;
import com.ttubeog.domain.spot.dto.request.UpdateSpotRequestDto;
import com.ttubeog.domain.spot.dto.response.SpotResponseDto;
import com.ttubeog.domain.spot.exception.AlreadyExistsSpotException;
import com.ttubeog.domain.spot.exception.InvalidDongAreaException;
import com.ttubeog.domain.spot.exception.InvalidImageListSizeException;
import com.ttubeog.global.config.security.token.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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


    /**
     * 산책 스팟 생성 API
     * @param request 유저 검증
     * @param createSpotRequestDto 산책 스팟 생성 DTO
     * @return ResponseEntity -> SpotResponseDto
     * @throws JsonProcessingException json processing 에러
     */
    @Operation(summary = "산책 스팟 생성",
            description = "산책 스팟을 생성합니다.\n" +
                    "사진은 1~10장 사이여야 합니다.",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SpotResponseDto.class))
                            )
                    }
                    ),
                    @ApiResponse(
                            responseCode = "500 - InvalidMemberException",
                            description = "멤버가 올바르지 않습니다.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = InvalidMemberException.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "500 - AlreadyExistsSpotException",
                            description = "이미 존재하는 산책 장소명입니다.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = AlreadyExistsSpotException.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "500 - InvalidDongAreaException",
                            description = "유효하지 않은 지역 정보입니다.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = InvalidDongAreaException.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "500 - InvalidImageListSizeException",
                            description = "유효하지 않은 이미지 개수입니다.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = InvalidImageListSizeException.class))
                                    )
                            }
                    )
    }
    )
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<?> createSpot(
            @CurrentUser HttpServletRequest request,
            @RequestBody CreateSpotRequestDto createSpotRequestDto
    ) throws JsonProcessingException {
        return spotService.createSpot(request, createSpotRequestDto);
    }


    /**
     * 산책 스팟 세부 사항 조회 API
     * @param request 유저 검증
     * @param spotId 산책 스팟 ID
     * @return ResponseEntity -> SpotResponseDto
     * @throws JsonProcessingException
     */
    @Operation(summary = "산책 스팟 세부 사항 조회 API",
            description = "산책 스팟을 세부 조회합니다.\n",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SpotResponseDto.class))
                            )
                    }
            ),
                    @ApiResponse(
                            responseCode = "500 - InvalidMemberException",
                            description = "멤버가 올바르지 않습니다.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = InvalidMemberException.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "500 - AlreadyExistsSpotException",
                            description = "이미 존재하는 산책 장소명입니다.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = AlreadyExistsSpotException.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "500 - InvalidDongAreaException",
                            description = "유효하지 않은 지역 정보입니다.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = InvalidDongAreaException.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "500 - InvalidImageListSizeException",
                            description = "유효하지 않은 이미지 개수입니다.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = InvalidImageListSizeException.class))
                                    )
                            }
                    )
            }
    )
    @GetMapping("/{spotId}")
    public ResponseEntity<?> findBySpotId(
            @CurrentUser HttpServletRequest request,
            @RequestParam(name = "spotId") Long spotId
    ) throws JsonProcessingException {
        return spotService.findBySpotId(request, spotId);
    }


    /**
     * 산책 스팟 수정 API
     * @param request 유저 검증
     * @param spotId 산책 스팟 ID
     * @param updateSpotRequestDto 산책스팟 업데이트 DTO
     * @return ResponseEntity -> SpotResponseDto
     * @throws JsonProcessingException JSON processing 에러
     */
    @Operation(summary = "산책 스팟 수정 API",
            description = "산책 스팟에 대한 정보를 수정해주세요.\n" +
                    "사진은 1~10장 사이여야 합니다.\n" +
                    "사진 수정은 아직 수정 중입니다.",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SpotResponseDto.class))
                            )
                    }
            ),
                    @ApiResponse(
                            responseCode = "500 - InvalidMemberException",
                            description = "멤버가 올바르지 않습니다.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = InvalidMemberException.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "500 - AlreadyExistsSpotException",
                            description = "이미 존재하는 산책 장소명입니다.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = AlreadyExistsSpotException.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "500 - InvalidDongAreaException",
                            description = "유효하지 않은 지역 정보입니다.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = InvalidDongAreaException.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "500 - InvalidImageListSizeException",
                            description = "유효하지 않은 이미지 개수입니다.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = InvalidImageListSizeException.class))
                                    )
                            }
                    )
            }
    )
    @PatchMapping
    public ResponseEntity<?> updateSpot(
            @CurrentUser HttpServletRequest request,
            @RequestParam(name = "spotId") Long spotId,
            @RequestBody UpdateSpotRequestDto updateSpotRequestDto
    ) throws JsonProcessingException {
        return spotService.updateSpot(request, spotId, updateSpotRequestDto);
    }

    /**
     * 산책 스팟 삭제 API
     * @param request 유저 검증
     * @param spotId 산책 스팟 ID
     * @return ResponseEntity -> SpotResponseDto
     * @throws JsonProcessingException JSON Processing 에러
     */
    @Operation(summary = "산책 스팟 삭제",
            description = "산책 스팟을 삭제합니다.\n" +
                    "산책 스팟 ID를 입력해주세요",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SpotResponseDto.class))
                            )
                    }
            ),
                    @ApiResponse(
                            responseCode = "500 - InvalidMemberException",
                            description = "멤버가 올바르지 않습니다.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = InvalidMemberException.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "500 - AlreadyExistsSpotException",
                            description = "이미 존재하는 산책 장소명입니다.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = AlreadyExistsSpotException.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "500 - InvalidDongAreaException",
                            description = "유효하지 않은 지역 정보입니다.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = InvalidDongAreaException.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "500 - InvalidImageListSizeException",
                            description = "유효하지 않은 이미지 개수입니다.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = InvalidImageListSizeException.class))
                                    )
                            }
                    )
            }
    )
    @DeleteMapping("/{spotId}")
    public ResponseEntity<?> deleteSpot(
            @CurrentUser HttpServletRequest request,
            @RequestParam(name = "spotId") Long spotId
    ) throws JsonProcessingException {
        return spotService.deleteSpot(request, spotId);
    }


    /**
     * 산책 스팟 방명록 작성 API
     * @param request 유저 검증
     * @param createGuestBookRequestDto 방명록 작성 DTO
     * @return ResponseEntity -> GuestBookResponseDto
     * @throws JsonProcessingException JSON Processing 에러
     */
    @Operation(summary = "산책 스팟 방명록 작성",
            description = "산책 스팟을 방명록을 작성합니다.\n" +
                    "GuestBook(방명록) 기능 미구현.",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = GuestBookService.class))
                            )
                    }
            ),
                    @ApiResponse(
                            responseCode = "500 - InvalidMemberException",
                            description = "멤버가 올바르지 않습니다.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = InvalidMemberException.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "500 - AlreadyExistsSpotException",
                            description = "이미 존재하는 산책 장소명입니다.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = AlreadyExistsSpotException.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "500 - InvalidDongAreaException",
                            description = "유효하지 않은 지역 정보입니다.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = InvalidDongAreaException.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "500 - InvalidImageListSizeException",
                            description = "유효하지 않은 이미지 개수입니다.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = InvalidImageListSizeException.class))
                                    )
                            }
                    )
            }
    )
    @PostMapping("/{spotId}/guestbook")
    public ResponseEntity<?> createGuestBook(
            @CurrentUser HttpServletRequest request,
            @RequestBody CreateGuestBookRequestDto createGuestBookRequestDto
    ) throws JsonProcessingException {
        return guestBookService.createGuestBook(request, createGuestBookRequestDto);
    }

    /**
     * 산책 스팟 좋아요 누르기 API
     * @param request 유저 검증
     * @param spotId 산책 스팟 ID
     * @return ResponseEntity
     * @throws JsonProcessingException JSON Processing 에러
     */
    @Operation(summary = "산책 스팟 좋아요 누르기",
            description = "산책 스팟에 좋아요를 누릅니다.",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SpotResponseDto.class))
                            )
                    }
            ),
                    @ApiResponse(
                            responseCode = "500 - InvalidMemberException",
                            description = "멤버가 올바르지 않습니다.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = InvalidMemberException.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "500 - AlreadyExistsSpotException",
                            description = "이미 존재하는 산책 장소명입니다.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = AlreadyExistsSpotException.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "500 - InvalidDongAreaException",
                            description = "유효하지 않은 지역 정보입니다.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = InvalidDongAreaException.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "500 - InvalidImageListSizeException",
                            description = "유효하지 않은 이미지 개수입니다.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = InvalidImageListSizeException.class))
                                    )
                            }
                    )
            }
    )
    @PatchMapping("/{spotId}/likes")
    public ResponseEntity<?> likeSpot(
            @CurrentUser HttpServletRequest request,
            @RequestParam(name = "spotId") Integer spotId
    ) throws JsonProcessingException {
        return spotService.likeSpot(request, spotId);
    }
}
