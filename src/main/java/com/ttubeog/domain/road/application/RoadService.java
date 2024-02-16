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
import com.ttubeog.domain.road.exception.DuplicateRoadNameException;
import com.ttubeog.domain.road.exception.InvalidRoadIdException;
import com.ttubeog.domain.road.exception.InvalidRoadTypeException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final RoadCoordinateRepository roadCoordinateRepository;
    private final SpotRepository spotRepository;
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public ResponseEntity<?> createRoad(HttpServletRequest request, CreateRoadRequestDto createRoadRequestDto) {

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

            if (roadRepository.findBySpotAndName(spot, createRoadRequestDto.getName()).isPresent()) {
                throw new DuplicateRoadNameException();
            }

            road = Road.builder()
                    .roadType(RoadType.SPOT)
                    .spot(spot)
                    .member(member)
                    .name(createRoadRequestDto.getName())
                    .roadCoordinateList(roadCoordinateList)
                    .build();

        } else if (createRoadRequestDto.getRoadType().equals(RoadType.STORE)) {

            store = storeRepository.findById(createRoadRequestDto.getStoreId()).orElseThrow(InvalidStoreIdException::new);

            if (roadRepository.findByStoreAndName(store, createRoadRequestDto.getName()).isPresent()) {
                throw new DuplicateRoadNameException();
            }

            road = Road.builder()
                    .roadType(RoadType.STORE)
                    .store(store)
                    .member(member)
                    .name(createRoadRequestDto.getName())
                    .roadCoordinateList(roadCoordinateList)
                    .build();
        } else {
            throw new InvalidRoadTypeException();
        }

        for (RoadCoordinate roadCoordinate : roadCoordinateList) {
            roadCoordinate.updateRoad(road);
        }

        roadRepository.save(road);

        roadCoordinateRepository.saveAll(roadCoordinateList);

        List<List<Double>> roadCoordinateDoubleListResponse = new ArrayList<>();
        for (RoadCoordinate roadCoordinate : road.getRoadCoordinateList()) {
            List<Double> roadCoordinateDoubleResponse = new ArrayList<>();
            roadCoordinateDoubleResponse.add(roadCoordinate.getLatitude());
            roadCoordinateDoubleResponse.add(roadCoordinate.getLongitude());
            roadCoordinateDoubleListResponse.add(roadCoordinateDoubleResponse);
        }

        RoadResponseDto roadResponseDto;

        if (road.getRoadType().equals(RoadType.SPOT)) {
            roadResponseDto = RoadResponseDto.builder()
                    .id(road.getId())
                    .roadType(RoadType.SPOT)
                    .spotId(road.getSpot().getId())
                    .memberId(road.getMember().getId())
                    .name(road.getName())
                    .roadCoordinateDoubleList(roadCoordinateDoubleListResponse)
                    .build();
        } else if (road.getRoadType().equals(RoadType.STORE)) {
            roadResponseDto = RoadResponseDto.builder()
                    .id(road.getId())
                    .roadType(RoadType.STORE)
                    .storeId(road.getStore().getId())
                    .memberId(road.getMember().getId())
                    .name(road.getName())
                    .roadCoordinateDoubleList(roadCoordinateDoubleListResponse)
                    .build();
        } else {
            throw new InvalidRoadTypeException();
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(roadResponseDto)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> findRoadBySpotId(HttpServletRequest request, Long spotId, Integer pageNum) {

        Long memberId = jwtTokenProvider.getMemberId(request);

        Member member = memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);

        Page<Road> roadPage = roadRepository.findAllBySpot_Id(spotId, PageRequest.of(pageNum, 1));

        List<RoadResponseDto> roadResponseDtoList = new ArrayList<>();

        for (Road road : roadPage) {
            List<List<Double>> roadCoordinateDoubleList = new ArrayList<>();
            for (RoadCoordinate roadCoordinate : road.getRoadCoordinateList()) {
                List<Double> roadCoordinateDouble = new ArrayList<>();
                roadCoordinateDouble.add(roadCoordinate.getLatitude());
                roadCoordinateDouble.add(roadCoordinate.getLongitude());
                roadCoordinateDoubleList.add(roadCoordinateDouble);
            }

            RoadResponseDto roadResponseDto = RoadResponseDto.builder()
                    .id(road.getId())
                    .roadType(road.getRoadType())
                    .spotId(road.getSpot().getId())
                    .memberId(road.getMember().getId())
                    .name(road.getName())
                    .roadCoordinateDoubleList(roadCoordinateDoubleList)
                    .build();
            roadResponseDtoList.add(roadResponseDto);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(roadResponseDtoList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> findRoadByStoreId(HttpServletRequest request, Long storeId, Integer pageNum) {

        Long memberId = jwtTokenProvider.getMemberId(request);

        Member member = memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);

        Page<Road> roadPage = roadRepository.findAllByStore_Id(storeId, PageRequest.of(pageNum, 1));

        List<RoadResponseDto> roadResponseDtoList = new ArrayList<>();

        for (Road road : roadPage) {
            List<List<Double>> roadCoordinateDoubleList = new ArrayList<>();
            for (RoadCoordinate roadCoordinate : road.getRoadCoordinateList()) {
                List<Double> roadCoordinateDouble = new ArrayList<>();
                roadCoordinateDouble.add(roadCoordinate.getLatitude());
                roadCoordinateDouble.add(roadCoordinate.getLongitude());
                roadCoordinateDoubleList.add(roadCoordinateDouble);
            }

            RoadResponseDto roadResponseDto = RoadResponseDto.builder()
                    .id(road.getId())
                    .roadType(road.getRoadType())
                    .storeId(road.getStore().getId())
                    .memberId(road.getMember().getId())
                    .name(road.getName())
                    .roadCoordinateDoubleList(roadCoordinateDoubleList)
                    .build();
            roadResponseDtoList.add(roadResponseDto);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(roadResponseDtoList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<?> deleteRoad(HttpServletRequest request, Long roadId) {

        Long memberId = jwtTokenProvider.getMemberId(request);

        Member member = memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);

        Road road = roadRepository.findById(roadId).orElseThrow(InvalidRoadIdException::new);

        if (!road.getMember().equals(member)) {
            throw new InvalidMemberException();
        }

        List<RoadCoordinate> roadCoordinateList = road.getRoadCoordinateList();

        roadRepository.delete(road);

        roadCoordinateRepository.deleteAll(roadCoordinateList);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("산책로를 삭제했습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
