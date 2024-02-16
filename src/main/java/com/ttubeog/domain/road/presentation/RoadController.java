package com.ttubeog.domain.road.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import com.ttubeog.domain.road.application.RoadService;
import com.ttubeog.domain.road.dto.request.CreateRoadRequestDto;
import com.ttubeog.domain.spot.dto.response.SpotResponseDto;
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
@RequestMapping("api/v1/road")
@RestController
public class RoadController {

    private final RoadService roadService;

    /**
     * 산책 스팟 생성 API
     * @param request 유저 검증
     * @param createRoadRequestDto 산책로 생성 DTO
     * @return ResponseEntity -> RoadResponseDto
     * @throws JsonProcessingException json processing 에러
     */
    @Operation(summary = "산책로 생성",
            description = "산책로를 생성합니다.",
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
     * 산책로 조회 API
     * @param request 유저 검증
     * @param roadId 산책로 Id
     * @return ResponseEntity -> RoadResponseDto
     * @throws JsonProcessingException json processing 에러
     */
    @Operation(summary = "산책로 조회",
            description = "산책로를 Id 값을 통해 조회합니다.",
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
                    )
            }
    )
    @GetMapping("/{roadId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> findRoadById(
            @CurrentUser HttpServletRequest request,
            @RequestParam(name = "roadId") Long roadId
    ) throws JsonProcessingException {
        return roadService.findById(request, roadId);
    }


    /**
     * 산책로 삭제 API
     * @param request 유저 검증
     * @param roadId 산책로 Id
     * @return ResponseEntity -> RoadResponseDto
     * @throws JsonProcessingException json processing 에러
     */
    @Operation(summary = "산책로 삭제",
            description = "산책로를 삭제합니다.",
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
