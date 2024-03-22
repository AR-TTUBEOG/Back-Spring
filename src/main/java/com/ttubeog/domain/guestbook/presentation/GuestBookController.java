package com.ttubeog.domain.guestbook.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttubeog.domain.guestbook.application.GuestBookService;
import com.ttubeog.domain.guestbook.dto.request.CreateGuestBookRequestDto;
import com.ttubeog.domain.guestbook.dto.response.GuestBookResponseDto;
import com.ttubeog.domain.member.exception.InvalidMemberException;
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

@Tag(name = "GuestBook", description = "GuestBook API(방명록 API)")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/guestbook")
public class GuestBookController {

    private final GuestBookService guestBookService;

    /**
     * 방명록 생성 API
     * @param request 유저 검증
     * @param createGuestBookRequestDto 방명록 생성 DTO
     * @return ResponseEntity -> GuestBookResponseDto
     * @throws JsonProcessingException json processing 에러
     */
    @Operation(summary = "방명록 생성 / createGuestBook",
            description = "방명록 생성 API 입니다.",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = GuestBookResponseDto.class))
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
                    )
            }
    )
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<?> createGuestBook(
            @CurrentUser HttpServletRequest request,
            @RequestBody CreateGuestBookRequestDto createGuestBookRequestDto
    ) throws JsonProcessingException {
        return guestBookService.createGuestBook(request, createGuestBookRequestDto);
    }


    /**
     * 산책 스팟에 속한 모든 방명록 페이징 조회 API
     * @param request
     * @param spotId
     * @param pageNum
     * @return
     * @throws JsonProcessingException
     */
    @Operation(summary = "산책 스팟 소속 방명록 조회 API / findGuestBookBySpotId",
            description = "산책 스팟에 속한 방명록을 페이징하여 조회하는 API 입니다.",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = GuestBookResponseDto.class))
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
                    )
            }
    )
    @GetMapping("/{spotId}&{pageNum}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> findGuestBookBySpotId(
            @CurrentUser HttpServletRequest request,
            @RequestParam(name = "spotId") Long spotId,
            @RequestParam(name = "pageNum") Integer pageNum
    ) throws JsonProcessingException {
        return guestBookService.findGuestBookBySpotId(request, spotId, pageNum);
    }


    /**
     * 매장에 속한 모든 방명록 페이징 조회 API
     * @param request
     * @param storeId
     * @param pageNum
     * @return
     * @throws JsonProcessingException
     */
    @Operation(summary = "매장 소속 방명록 조회 API / findGuestBookByStoreId",
            description = "매장에 속한 방명록을 페이징하여 조회하는 API 입니다.",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = GuestBookResponseDto.class))
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
                    )
            }
    )
    @GetMapping("/{storeId}&{pageNum}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> findGuestBookByStoreId(
            @CurrentUser HttpServletRequest request,
            @RequestParam(name = "storeId") Long storeId,
            @RequestParam(name = "pageNum") Integer pageNum
    ) throws JsonProcessingException {
        return guestBookService.findGuestBookByStoreId(request, storeId, pageNum);
    }


    /**
     * 방명록 삭제 API
     * @param request
     * @param guestBookId
     * @return
     * @throws JsonProcessingException
     */
    @Operation(summary = "방명록 삭제 API / deleteGuestBook",
            description = " 방명록을 삭제하는 API 입니다.",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ResponseEntity.class))
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
                    )
            }
    )
    @DeleteMapping("/{guestBookId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> deleteGuestBook(
            @CurrentUser HttpServletRequest request,
            @RequestParam(name = "guestBookId") Long guestBookId
    ) throws  JsonProcessingException {
        return guestBookService.deleteGuestBook(request, guestBookId);
    }

}
