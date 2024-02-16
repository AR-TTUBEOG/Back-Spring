package com.ttubeog.domain.road.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttubeog.domain.road.application.RoadService;
import com.ttubeog.domain.road.dto.request.CreateRoadRequestDto;
import com.ttubeog.global.config.security.token.CurrentUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.criteria.CriteriaBuilder;
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

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<?> createRoad(
            @CurrentUser HttpServletRequest request,
            @RequestBody CreateRoadRequestDto createRoadRequestDto
    ) throws JsonProcessingException {
        return roadService.createRoad(request, createRoadRequestDto);
    }

    @GetMapping("/{spotId}&{pageNum}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> findRoadBySpotId(
            @CurrentUser HttpServletRequest request,
            @RequestParam(name = "spotId") Long spotId,
            @RequestParam(name = "pageNum") Integer pageNum
    ) throws JsonProcessingException {
        return roadService.findRoadBySpotId(request, spotId, pageNum);
    }

    @GetMapping("/{storeId}&{pageNum}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> findRoadByStoreId(
            @CurrentUser HttpServletRequest request,
            @RequestParam(name = "storeId") Long storeId,
            @RequestParam(name = "pageNum") Integer pageNum
    ) throws JsonProcessingException {
        return roadService.findRoadByStoreId(request, storeId, pageNum);
    }

    @DeleteMapping("/{roadId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> deleteRoad(
            @CurrentUser HttpServletRequest request,
            @RequestParam(name = "roadId") Long roadId
    ) throws JsonProcessingException {
        return roadService.deleteRoad(request, roadId);
    }
}
