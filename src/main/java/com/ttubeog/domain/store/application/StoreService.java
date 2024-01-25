package com.ttubeog.domain.store.application;

import com.ttubeog.domain.area.domain.DongArea;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import com.ttubeog.domain.store.domain.Store;
import com.ttubeog.domain.store.domain.repository.StoreRepository;
import com.ttubeog.domain.store.dto.request.RegisterStoreReq;
import com.ttubeog.domain.store.dto.response.RegisterStoreRes;
import com.ttubeog.global.config.security.token.UserPrincipal;
import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.global.payload.ApiResponse;
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

    // 매장 등록
    @Transactional
    public ResponseEntity<?> registerStore(UserPrincipal userPrincipal, RegisterStoreReq registerStoreReq) {

        Member member = memberRepository.findById(userPrincipal.getId()).orElseThrow(InvalidMemberException::new);

        Store store = Store.builder()
                .name(registerStoreReq.getName())
                .info(registerStoreReq.getInfo())
                .dongArea(new DongArea(registerStoreReq.getDongAreaId()))
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

}
