package com.ttubeog.domain.road.application;

import com.ttubeog.domain.auth.security.JwtTokenProvider;
import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import com.ttubeog.domain.road.domain.Road;
import com.ttubeog.domain.road.domain.RoadType;
import com.ttubeog.domain.road.domain.repository.RoadRepository;
import com.ttubeog.domain.road.dto.request.CreateRoadRequestDto;
import com.ttubeog.domain.road.dto.response.RoadResponseDto;
import com.ttubeog.domain.road.exception.InvalidRoadIdException;
import com.ttubeog.domain.road.exception.InvalidRoadTypeException;
import com.ttubeog.domain.road.exception.NullRoadCoordinateException;
import com.ttubeog.domain.roadcoordinate.domain.RoadCoordinate;
import com.ttubeog.domain.roadcoordinate.domain.repository.RoadCoordinateRepository;
import com.ttubeog.domain.spot.domain.Spot;
import com.ttubeog.domain.spot.domain.repository.SpotRepository;
import com.ttubeog.domain.spot.exception.InvalidSpotIdException;
import com.ttubeog.domain.store.domain.Store;
import com.ttubeog.domain.store.domain.repository.StoreRepository;
import com.ttubeog.domain.store.exception.InvalidStoreIdException;
import com.ttubeog.global.payload.ApiResponse;
import com.ttubeog.global.payload.Message;
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

    private final JwtTokenProvider jwtTokenProvider;

    private final RoadRepository roadRepository;
    private final RoadCoordinateRepository roadCoordinateRepository;
    private final MemberRepository memberRepository;
    private final SpotRepository spotRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public ResponseEntity<?> createRoad(HttpServletRequest request, CreateRoadRequestDto createRoadRequestDto) {

        Long memberId = jwtTokenProvider.getMemberId(request);

        Member member = memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);

        Road road;

        List<RoadCoordinate> roadCoordinateList = new ArrayList<>();
        List<List<Float>> createRoadRequestDtoRoadCoordinate = createRoadRequestDto.getRoadCoordinate();
        for (List<Float> roadCoordinateDto : createRoadRequestDtoRoadCoordinate) {
            RoadCoordinate roadCoordinate = RoadCoordinate.builder()
                    .latitude(roadCoordinateDto.get(0))
                    .longitude(roadCoordinateDto.get(1))
                    .build();
            roadCoordinateList.add(roadCoordinate);
        }

        if (roadCoordinateList.isEmpty()) {
            throw new NullRoadCoordinateException();
        }


        if (createRoadRequestDto.getRoadType().equals(RoadType.SPOT)) {
            Spot spot = spotRepository.findById(createRoadRequestDto.getSpotId()).orElseThrow(InvalidSpotIdException::new);
            road = Road.builder()
                    .roadType(RoadType.SPOT)
                    .spot(spot)
                    .member(member)
                    .name(createRoadRequestDto.getName())
                    .roadCoordinateList(roadCoordinateList)
                    .time(createRoadRequestDto.getTime())
                    .build();
            roadRepository.save(road);
        } else if (createRoadRequestDto.getRoadType().equals(RoadType.STORE)) {
            Store store = storeRepository.findById(createRoadRequestDto.getStoreId()).orElseThrow(InvalidStoreIdException::new);
            road = Road.builder()
                    .roadType(RoadType.STORE)
                    .store(store)
                    .member(member)
                    .name(createRoadRequestDto.getName())
                    .roadCoordinateList(roadCoordinateList)
                    .time(createRoadRequestDto.getTime())
                    .build();
            roadRepository.save(road);
        } else {
            throw new InvalidRoadTypeException();
        }

        for (RoadCoordinate roadCoordinate: roadCoordinateList) {
            roadCoordinate.updateRoad(road);
        }

        roadCoordinateRepository.saveAll(roadCoordinateList);

        RoadResponseDto roadResponseDto = RoadResponseDto.builder()
                .id(road.getId())
                .roadType(road.getRoadType())
                .spot(road.getSpot())
                .store(road.getStore())
                .member(road.getMember())
                .name(road.getName())
                .roadCoordinate(road.getRoadCoordinateList())
                .time(road.getTime())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(roadResponseDto)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> findById(HttpServletRequest request, Long roadId) {

        Long memberId = jwtTokenProvider.getMemberId(request);

        Member member = memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);

        Road road = roadRepository.findById(roadId).orElseThrow(InvalidRoadIdException::new);

        RoadResponseDto roadResponseDto = RoadResponseDto.builder()
                .id(road.getId())
                .roadType(road.getRoadType())
                .spot(road.getSpot())
                .store(road.getStore())
                .member(road.getMember())
                .name(road.getName())
                .roadCoordinate(road.getRoadCoordinateList())
                .time(road.getTime())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(roadResponseDto)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<?> deleteRoad(HttpServletRequest request, Long roadId) {

        Long memberId = jwtTokenProvider.getMemberId(request);

        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);

        Road road = roadRepository.findById(roadId).orElseThrow(InvalidRoadIdException::new);

        List<RoadCoordinate> roadCoordinateList = roadCoordinateRepository.findByRoad(road);

        roadRepository.delete(road);

        roadCoordinateRepository.deleteAll(roadCoordinateList);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("산책로를 삭제햇습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
