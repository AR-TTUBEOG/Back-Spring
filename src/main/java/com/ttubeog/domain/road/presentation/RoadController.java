package com.ttubeog.domain.road.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import com.ttubeog.domain.road.application.RoadService;
import com.ttubeog.domain.road.domain.repository.RoadRepository;
import com.ttubeog.domain.road.dto.request.CreateRoadRequestDto;
import com.ttubeog.domain.road.dto.response.RoadResponseDto;
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

@Tag(name = "Road", description = "Road API(산책로 API)")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/road")
public class RoadController {

    private final RoadService roadService;

    /**
     * 산책로 생성 API
     * @param request
     * @param createRoadRequestDto
     * @return
     * @throws JsonProcessingException
     */
    @Operation(summary = "산책로 생성 API / createRoad",
            description = "산책로를 생성하는 API 입니다.",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = RoadResponseDto.class))
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
    public ResponseEntity<?> createRoad(
            @CurrentUser HttpServletRequest request,
            @RequestBody CreateRoadRequestDto createRoadRequestDto
    ) throws JsonProcessingException {
        return roadService.createRoad(request, createRoadRequestDto);
    }

    /**
     * 산책 스팟에 속한 산책로 페이징 조회 API
     * @param request
     * @param spotId
     * @param pageNum
     * @return
     * @throws JsonProcessingException
     */
    @Operation(summary = "산책 스팟 소속 산책로 페이징 조회 API / findRoadBySpotId",
            description = "산책 스팟에 속한 산책로를 페이징하여 조회하는 API 입니다.",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = RoadRepository.class))
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
    public ResponseEntity<?> findRoadBySpotId(
            @CurrentUser HttpServletRequest request,
            @RequestParam(name = "spotId") Long spotId,
            @RequestParam(name = "pageNum") Integer pageNum
    ) throws JsonProcessingException {
        return roadService.findRoadBySpotId(request, spotId, pageNum);
    }


    /**
     * 매장 소속 산책로 페이징 조회 API
     * @param request
     * @param storeId
     * @param pageNum
     * @return
     * @throws JsonProcessingException
     */
    @Operation(summary = "매장 소속 산책로 페이징 조회 API / findRoadByStoreId",
            description = "매장에 속한 산책로를 페이징하여 조회하는 API 입니다.",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = RoadRepository.class))
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
    public ResponseEntity<?> findRoadByStoreId(
            @CurrentUser HttpServletRequest request,
            @RequestParam(name = "storeId") Long storeId,
            @RequestParam(name = "pageNum") Integer pageNum
    ) throws JsonProcessingException {
        return roadService.findRoadByStoreId(request, storeId, pageNum);
    }


    /**
     * 산책로 삭제 API
     * @param request
     * @param roadId
     * @return
     * @throws JsonProcessingException
     */
    @Operation(summary = "산책로 삭제 API / deleteRoadgi",
            description = "산책로를 삭제하는 API 입니다.",
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
    @DeleteMapping("/{roadId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> deleteRoad(
            @CurrentUser HttpServletRequest request,
            @RequestParam(name = "roadId") Long roadId
    ) throws JsonProcessingException {
        return roadService.deleteRoad(request, roadId);
    }
}
