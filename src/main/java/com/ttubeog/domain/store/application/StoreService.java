package com.ttubeog.domain.store.application;

import com.ttubeog.domain.area.domain.DongArea;
import com.ttubeog.domain.area.domain.repository.DongAreaRepository;
import com.ttubeog.domain.auth.security.JwtTokenProvider;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import com.ttubeog.domain.store.domain.Store;
import com.ttubeog.domain.store.domain.repository.StoreRepository;
import com.ttubeog.domain.store.dto.request.RegisterStoreReq;
import com.ttubeog.domain.store.dto.request.UpdateStoreReq;
import com.ttubeog.domain.store.dto.response.GetStoreDetailRes;
import com.ttubeog.domain.store.dto.response.RegisterStoreRes;
import com.ttubeog.domain.store.dto.response.UpdateStoreRes;
import com.ttubeog.domain.store.exception.InvalidDongAreaException;
import com.ttubeog.domain.store.exception.UnathorizedMemberException;
import com.ttubeog.domain.store.exception.NonExistentStoreException;
import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.global.payload.ApiResponse;
import com.ttubeog.global.payload.Message;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;
    private final DongAreaRepository dongAreaRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // 매장 등록
    @Transactional
    public ResponseEntity<?> registerStore(HttpServletRequest request, RegisterStoreReq registerStoreReq) {

        Long memberId = jwtTokenProvider.getMemberId(request);
        Member member = memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);

        DongArea dongArea = dongAreaRepository.findById(registerStoreReq.getDongAreaId()).orElseThrow(InvalidDongAreaException::new);

        Store store = Store.builder()
                .name(registerStoreReq.getName())
                .info(registerStoreReq.getInfo())
                .dongArea(dongArea)
                .member(member)
                .detailAddress(registerStoreReq.getDetailAddress())
                .latitude(registerStoreReq.getLatitude())
                .longitude(registerStoreReq.getLongitude())
                .image(registerStoreReq.getImage())
                .type(registerStoreReq.getType())
                .build();

        storeRepository.save(store);

        RegisterStoreRes registerStoreRes = RegisterStoreRes.builder()
                .storeId(store.getId())
                .memberId(member.getId())
                .name(store.getName())
                .info(store.getInfo())
                .dongAreaId(store.getDongArea().getId())
                .detailAddress(store.getDetailAddress())
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
                .image(store.getImage())
                .type(store.getType())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(registerStoreRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 매장 수정
    @Transactional
    public ResponseEntity<?> updateStore(HttpServletRequest request, UpdateStoreReq updateStoreReq) {

        Long memberId = jwtTokenProvider.getMemberId(request);
        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);
        Store store = storeRepository.findById(updateStoreReq.getStoreId()).orElseThrow(NonExistentStoreException::new);

        Long storeOwnerId = store.getMember().getId();
        if (!storeOwnerId.equals(memberId)) {
            throw new UnathorizedMemberException();
        }

        store.updateName(updateStoreReq.getName());
        store.updateInfo(updateStoreReq.getInfo());
        store.updateDetailAddress(updateStoreReq.getDetailAddress());
        store.updateLatitude(updateStoreReq.getLatitude());
        store.updateLongitude(updateStoreReq.getLongitude());
        store.updateImage(updateStoreReq.getImage());
        store.updateType(updateStoreReq.getType());

        UpdateStoreRes updateStoreRes = UpdateStoreRes.builder()
                .storeId(store.getId())
                .name(store.getName())
                .info(store.getInfo())
                .detailAddress(store.getDetailAddress())
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
                .image(store.getImage())
                .type(store.getType())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(updateStoreRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 매장 삭제
    @Transactional
    public ResponseEntity<?> deleteStore(HttpServletRequest request, Long storeId) {

        Long memberId = jwtTokenProvider.getMemberId(request);
        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);
        Store store = storeRepository.findById(storeId).orElseThrow(NonExistentStoreException::new);

        Long storeOwnerId = store.getMember().getId();
        if (!storeOwnerId.equals(memberId)) {
            throw new UnathorizedMemberException();
        }

        storeRepository.delete(store);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("매장 정보가 정상적으로 삭제되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 매장 세부사항 조회
    public ResponseEntity<?> getStoreDetails(HttpServletRequest request, Long storeId) {

        Long memberId = jwtTokenProvider.getMemberId(request);
        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);
        Store store = storeRepository.findById(storeId).orElseThrow(NonExistentStoreException::new);

        // List<BenefitType> storeBenefits = benefitRepository.findTypeByStoreId(storeId);
        // Integer guestbookCount = guestBookRepository.countByStoreId(storeId);
        // Integer likesCount = likesRepository.countByStoreId(storeId);

        GetStoreDetailRes getStoreDetailRes = GetStoreDetailRes.builder()
                .storeId(storeId)
                .memberId(store.getMember().getId())
                .name(store.getName())
                .info(store.getInfo())
                .dongAreaId(store.getDongArea().getId())
                .detailAddress(store.getDetailAddress())
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
                .image(store.getImage())
                .stars(store.getStars())
                .type(store.getType())
                //.storeBenefits(storeBenefits.stream().map(BenefitType::getType).collect(Collectors.toList()))
                //.guestbookCount(guestbookCount)
                //.likesCount(likesCount)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(getStoreDetailRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
