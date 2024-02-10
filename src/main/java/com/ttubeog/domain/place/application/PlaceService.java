package com.ttubeog.domain.place.application;

import com.ttubeog.domain.auth.config.SecurityUtil;
import com.ttubeog.domain.likes.domain.repository.LikesRepository;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import com.ttubeog.domain.place.domain.PlaceType;
import com.ttubeog.domain.place.dto.request.GetNearbyPlaceReq;
import com.ttubeog.domain.place.dto.response.GetAllPlaceRes;
import com.ttubeog.domain.spot.domain.Spot;
import com.ttubeog.domain.spot.domain.repository.SpotRepository;
import com.ttubeog.domain.store.domain.Store;
import com.ttubeog.domain.store.domain.repository.StoreRepository;
import com.ttubeog.global.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PlaceService {

    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final SpotRepository spotRepository;
    private final LikesRepository likesRepository;

    public List<GetAllPlaceRes> getPageOfPlaces(List<GetAllPlaceRes> places, int page, int size) {
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, places.size());

        return places.subList(startIndex, endIndex);
    }

    public List<GetAllPlaceRes> getAllPlaceResList(Pageable pageable) {

        List<GetAllPlaceRes> places = new ArrayList<>();

        List<Store> stores = storeRepository.findAll();
        places.addAll(stores.stream().map(this::mapStoreToDto).collect(Collectors.toList()));
        // List<Spot> spots = spotRepository.findAll();
        // places.addAll(spots.stream().map(this::mapSpotToDto).collect(Collectors.toList()));
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();

        return getPageOfPlaces(places, page, size);
    }

    private GetAllPlaceRes mapStoreToDto(Store store) {

        final long memberId = SecurityUtil.getCurrentMemeberId();
        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);
        Boolean storeLiked = likesRepository.existsByMemberIdAndStoreId(memberId, store.getId());
        PlaceType placeType = new PlaceType(true, false);

        return GetAllPlaceRes.builder()
                .placeId(store.getId())
                .placeType(placeType)
                .memberId(store.getMember().getId())
                .name(store.getName())
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
                //.image(store.getImage())
                .stars(store.getStars())
                // .guestbookCount(guestRepository.countByStoreId(store.getID()))
                .isFavorited(storeLiked)
                .createdAt(store.getCreatedAt())
                .build();
    }

    /*private GetAllPlaceRes mapSpotToDto(Spot spot) {

        final long memberId = SecurityUtil.getCurrentMemberId();
        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);
        Boolean spotLiked = likesRepository.existsByMemberIdAndSpotId(memberId, spot.getId());
        PlaceType placeType = new PlaceType(false, true);

        return GetAllPlaceRes.builder()
                .placeId(spot.getId())
                .placeType(placeType)
                .memberId(spot.getMember().getId())
                .name(spot.getName())
                .latitude(spot.getLatitude())
                .longitude(spot.getLongitude())
                //.image(spot.getImage())
                .stars(spot.getStars())
                // .guestbookCount(guestRepository.countBySpotId(spot.getID()))
                .isFavorited(spotLiked)
                .createdAt(spot.getCreatedAt())
                .build();
    }*/

    // 전체 조회
    @Transactional
    public ResponseEntity<?> getAllPlaces(Pageable pageable) {

        final long memberId = SecurityUtil.getCurrentMemeberId();
        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);

        List<GetAllPlaceRes> allPlaces = getAllPlaceResList(pageable);

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
    public ResponseEntity<?> getAllPlacesRecommended(Pageable pageable) {

        final long memberId = SecurityUtil.getCurrentMemeberId();
        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);
        List<GetAllPlaceRes> allPlaces = getAllPlaceResList(pageable);

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
    public ResponseEntity<?> getAllPlacesNearby(GetNearbyPlaceReq getNearbyPlaceReq, Pageable pageable) {

        final long memberId = SecurityUtil.getCurrentMemeberId();
        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);
        List<GetAllPlaceRes> allPlaces = getAllPlaceResList(pageable);

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
    public ResponseEntity<?> getAllPlacesLatest(Pageable pageable) {

        final long memberId = SecurityUtil.getCurrentMemeberId();
        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);
        List<GetAllPlaceRes> allPlaces = getAllPlaceResList(pageable);
        allPlaces.sort(Comparator.comparing(GetAllPlaceRes::getCreatedAt).reversed());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(allPlaces)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
