package com.ttubeog.domain.road.application;

import com.ttubeog.domain.road.dto.request.CreateRoadRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RoadService {
    public ResponseEntity<?> createGuestBook(HttpServletRequest request, CreateRoadRequestDto createRoadRequestDto) {

        return null;
    }

    public ResponseEntity<?> findRoadBySpotId(HttpServletRequest request, Long spotId, Long pageNum) {

        return null;
    }

    public ResponseEntity<?> findRoadByStoreId(HttpServletRequest request, Long storeId, Long pageNum) {

        return null;
    }

    public ResponseEntity<?> deleteRoad(HttpServletRequest request, Long roadId) {

        return null;
    }
}
