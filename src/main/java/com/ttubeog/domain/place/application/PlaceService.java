package com.ttubeog.domain.place.application;

import com.ttubeog.domain.auth.config.SecurityUtil;
import com.ttubeog.domain.auth.security.JwtTokenProvider;
import com.ttubeog.domain.image.application.ImageService;
import com.ttubeog.domain.image.domain.repository.ImageRepository;
import com.ttubeog.domain.likes.domain.repository.LikesRepository;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import com.ttubeog.domain.place.domain.PlaceType;
import com.ttubeog.domain.place.dto.request.GetNearbyPlaceReq;
import com.ttubeog.domain.place.dto.request.SearchPlaceReq;
import com.ttubeog.domain.place.dto.response.GetAllPlaceRes;
import com.ttubeog.domain.place.dto.response.SearchPlaceRes;
import com.ttubeog.domain.spot.domain.Spot;
import com.ttubeog.domain.spot.domain.repository.SpotRepository;
import com.ttubeog.domain.store.domain.Store;
import com.ttubeog.domain.store.domain.repository.StoreRepository;
import com.ttubeog.global.payload.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.ttubeog.domain.image.application.ImageService.getImageString;


@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PlaceService {

    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final SpotRepository spotRepository;
    private final LikesRepository likesRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;
    private final JwtTokenProvider jwtTokenProvider;

    public List<GetAllPlaceRes> getPageOfPlaces(List<GetAllPlaceRes> places, int page, int size) {
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, places.size());

        return places.subList(startIndex, endIndex);
    }

    public List<GetAllPlaceRes> getAllPlaceResList(HttpServletRequest request, Pageable pageable) {

        List<GetAllPlaceRes> places = new ArrayList<>();

        List<Store> stores = storeRepository.findAll();
        places.addAll(stores.stream().map(store -> mapStoreToDto(request, store)).collect(Collectors.toList()));
        List<Spot> spots = spotRepository.findAll();
        places.addAll(spots.stream().map(spot -> mapSpotToDto(request, spot)).collect(Collectors.toList()));
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();

        return getPageOfPlaces(places, page, size);
    }

    private GetAllPlaceRes mapStoreToDto(HttpServletRequest request, Store store) {

        Long memberId = jwtTokenProvider.getMemberId(request);
        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);

        // 현재 로그인 유저의 좋아요 여부
        Boolean storeLiked = likesRepository.existsByMemberIdAndStoreId(memberId, store.getId());
        PlaceType placeType = new PlaceType(true, false);

        return GetAllPlaceRes.builder()
                .placeId(store.getId())
                .placeType(placeType)
                .memberId(store.getMember().getId())
                .name(store.getName())
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
                // .image(store.getImage())
                .stars(store.getStars())
                // .guestbookCount(guestRepository.countByStoreId(store.getID()))
                .isFavorited(storeLiked)
                .createdAt(store.getCreatedAt())
                .build();
    }

    private GetAllPlaceRes mapSpotToDto(HttpServletRequest request, Spot spot) {

        Long memberId = jwtTokenProvider.getMemberId(request);
        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);

        // 현재 로그인 유저의 좋아요 여부
        Boolean spotLiked = likesRepository.existsByMemberIdAndSpotId(memberId, spot.getId());
        PlaceType placeType = new PlaceType(false, true);

        return GetAllPlaceRes.builder()
                .placeId(spot.getId())
                .placeType(placeType)
                .memberId(spot.getMember().getId())
                .name(spot.getName())
                //위도, 경도 double로 변경 필요
                //.latitude(spot.getLatitude())
                //.longitude(spot.getLongitude())
                //.image(spot.getImage())
                .stars(spot.getStars())
                //.guestbookCount(guestRepository.countBySpotId(spot.getID()))
                .isFavorited(spotLiked)
                .createdAt(spot.getCreatedAt())
                .build();
    }

    // 전체 조회
    @Transactional
    public ResponseEntity<?> getAllPlaces(HttpServletRequest request, Pageable pageable) {

        Long memberId = jwtTokenProvider.getMemberId(request);
        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);

        List<GetAllPlaceRes> allPlaces = getAllPlaceResList(request, pageable);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(allPlaces)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    public int calculateRecommendationScore(float stars, int guestbookCount, int likesCount) {

        int starsScore = (int) (stars * 20);
        int guestbookScore = calculateGuestbookScore(guestbookCount);
        int likesScore = calculateLikesStore(likesCount);
        int totalScore = starsScore + guestbookScore + likesScore;

        return totalScore;
    }

    private int calculateGuestbookScore(int guestbookCount) {

        if (guestbookCount <= 10) {
            return 10;
        } else if (guestbookCount <= 50) {
            return 30;
        } else {
            return 50;
        }
    }

    private int calculateLikesStore(int likesCount) {

        if (likesCount <= 100) {
            return 10;
        } else if (likesCount <= 500) {
            return 30;
        } else {
            return 50;
        }
    }

    // 추천순 조회
    @Transactional
    public ResponseEntity<?> getAllPlacesRecommended(HttpServletRequest request, Pageable pageable) {

        Long memberId = jwtTokenProvider.getMemberId(request);
        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);
        List<GetAllPlaceRes> allPlaces = getAllPlaceResList(request, pageable);

        for (GetAllPlaceRes place : allPlaces) {
            int recommendationScore = calculateRecommendationScore(place.getStars(), place.getGuestbookCount(), place.getLikesCount());
            place.setRecommendationScore(recommendationScore);
        }

        allPlaces.sort(Comparator.comparingInt(GetAllPlaceRes::getRecommendationScore).reversed());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(allPlaces)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    private double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        double R = 6371; // 지구 반지름

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)); // 단위 km
        double distance = R * c * 1000; // 단위 m

        return distance;
    }

    // 거리순 조회
    @Transactional
    public ResponseEntity<?> getAllPlacesNearby(HttpServletRequest request, GetNearbyPlaceReq getNearbyPlaceReq, Pageable pageable) {

        Long memberId = jwtTokenProvider.getMemberId(request);
        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);
        List<GetAllPlaceRes> allPlaces = getAllPlaceResList(request, pageable);

        Double userLatitude = getNearbyPlaceReq.getLatitude();
        Double userLongitude = getNearbyPlaceReq.getLongitude();

        for (GetAllPlaceRes place: allPlaces) {
            double distance = calculateDistance(userLatitude, userLongitude, place.getLatitude(), place.getLongitude());
            place.setDistance(distance);
        }

        allPlaces.sort(Comparator.comparingDouble(GetAllPlaceRes::getDistance));

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(allPlaces)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 최신순 조회
    @Transactional
    public ResponseEntity<?> getAllPlacesLatest(HttpServletRequest request, Pageable pageable) {

        Long memberId = jwtTokenProvider.getMemberId(request);
        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);
        List<GetAllPlaceRes> allPlaces = getAllPlaceResList(request, pageable);
        allPlaces.sort(Comparator.comparing(GetAllPlaceRes::getCreatedAt).reversed());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(allPlaces)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 장소 검색
    public ResponseEntity<?> searchPlaces(HttpServletRequest request, SearchPlaceReq searchPlaceReq, Pageable pageable) {

        Long memberId = jwtTokenProvider.getMemberId(request);
        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);
        List<GetAllPlaceRes> allPlaces = getAllPlaceResList(request, pageable);

        // 검색어 포함 + 동일한 동ID 가진 장소 필터링
        List<GetAllPlaceRes> searchResult = new ArrayList<>();
        for (GetAllPlaceRes place : allPlaces) {
            if (place.getName().contains(searchPlaceReq.getKeyword()) ||
                    (place.getDongAreaId() != null && place.getDongAreaId().equals(searchPlaceReq.getDongAreaId()))) {
                searchResult.add(place);
            }
        }

        // 이름에 검색어가 포함된 장소들을 우선적으로 정렬
        Collections.sort(searchResult, Comparator.comparing(place -> {
            if (place.getName().contains(searchPlaceReq.getKeyword())) {
                return 0;
            } else {
                return 1;
            }
        }));

        // 페이징
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        List<GetAllPlaceRes> paginatedResult = searchResult.stream()
                .skip(page * size)
                .limit(size)
                .collect(Collectors.toList());

        // 반환할 데이터가 없으면 빈 목록 반환
        if (paginatedResult.isEmpty()) {
            return ResponseEntity.ok().body(Collections.emptyList());
        }

        List<SearchPlaceRes> searchPlaceRes = paginatedResult.stream()
                .map(place -> SearchPlaceRes.builder()
                        .placeId(place.getPlaceId())
                        .placeType(place.getPlaceType())
                        .dongAreaId(place.getDongAreaId())
                        .memberId(place.getMemberId())
                        .name(place.getName())
                        .latitude(place.getLatitude())
                        .longitude(place.getLongitude())
                        .image(place.getImage())
                        .stars(place.getStars())
                        .guestbookCount(place.getGuestbookCount())
                        .likesCount(place.getLikesCount())
                        .isFavorited(place.getIsFavorited())
                        .build())
                .collect(Collectors.toList());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(searchPlaceRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
