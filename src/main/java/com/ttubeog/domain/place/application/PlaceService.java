package com.ttubeog.domain.place.application;

import com.ttubeog.domain.auth.config.SecurityUtil;
import com.ttubeog.domain.likes.domain.repository.LikesRepository;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import com.ttubeog.domain.place.domain.PlaceType;
import com.ttubeog.domain.place.dto.response.GetAllPlaceRes;
import com.ttubeog.domain.spot.domain.Spot;
import com.ttubeog.domain.spot.domain.repository.SpotRepository;
import com.ttubeog.domain.store.domain.Store;
import com.ttubeog.domain.store.domain.repository.StoreRepository;
import com.ttubeog.global.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
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

    public List<GetAllPlaceRes> getAllPlaceResList() {

        List<GetAllPlaceRes> places = new ArrayList<>();

        List<Store> stores = storeRepository.findAll();
        places.addAll(stores.stream().map(this::mapStoreToDto).collect(Collectors.toList()));
        // List<Spot> spots = spotRepository.findAll();
        // places.addAll(spots.stream().map(this::mapSpotToDto).collect(Collectors.toList()));

        return places;
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
    public ResponseEntity<?> getAllPlaces() {

        final long memberId = SecurityUtil.getCurrentMemeberId();
        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);
        List<GetAllPlaceRes> allPlaces = getAllPlaceResList();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(allPlaces)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
