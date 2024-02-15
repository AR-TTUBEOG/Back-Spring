package com.ttubeog.domain.road.application;

import com.ttubeog.domain.auth.security.JwtTokenProvider;
import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import com.ttubeog.domain.road.domain.Road;
import com.ttubeog.domain.road.domain.RoadType;
import com.ttubeog.domain.road.domain.repository.RoadRepository;
import com.ttubeog.domain.road.dto.request.CreateRoadRequestDto;
import com.ttubeog.domain.road.exception.DuplicateRoadNameException;
import com.ttubeog.domain.road.exception.InvalidRoadTypeException;
import com.ttubeog.domain.road.presentation.RoadController;
import com.ttubeog.domain.roadcoordinate.domain.RoadCoordinate;
import com.ttubeog.domain.spot.domain.Spot;
import com.ttubeog.domain.spot.domain.repository.SpotRepository;
import com.ttubeog.domain.spot.exception.InvalidSpotIdException;
import com.ttubeog.domain.store.domain.Store;
import com.ttubeog.domain.store.domain.repository.StoreRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RoadService {

    private final RoadRepository roadRepository;
    private final SpotRepository spotRepository;
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    private final JwtTokenProvider jwtTokenProvider;

    public ResponseEntity<?> createGuestBook(HttpServletRequest request, CreateRoadRequestDto createRoadRequestDto) {

        Long memberId = jwtTokenProvider.getMemberId(request);

        Member member = memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);

        Road road;
        Spot spot;
        Store store;

        List<RoadCoordinate> roadCoordinateList = new ArrayList<>();
        List<List<Double>> roadCoordinateDoubleList = createRoadRequestDto.getRoadCoordinateList();
        for (List<Double> roadCoordinateDouble : roadCoordinateDoubleList) {
            RoadCoordinate roadCoordinate = RoadCoordinate.builder()
                    .latitude(roadCoordinateDouble.get(0))
                    .longitude(roadCoordinateDouble.get(1))
                    .build();
            roadCoordinateList.add(roadCoordinate);
        }

        if (createRoadRequestDto.getRoadType().equals(RoadType.SPOT)) {

            spot = spotRepository.findById(createRoadRequestDto.getSpotId()).orElseThrow(InvalidSpotIdException::new);

            roadRepository.findBySpotAndName(spot, createRoadRequestDto.getName()).orElseThrow(DuplicateRoadNameException::new);

            road = Road.builder().build();

        } else if (createRoadRequestDto.getRoadType().equals(RoadType.STORE)) {

        } else {
            throw new InvalidRoadTypeException();
        }



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
