package com.ttubeog.domain.store.application;

import com.ttubeog.domain.area.domain.DongArea;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import com.ttubeog.domain.store.domain.Store;
import com.ttubeog.domain.store.domain.repository.StoreRepository;
import com.ttubeog.domain.store.dto.request.RegisterStoreReq;
import com.ttubeog.domain.store.dto.request.UpdateStoreReq;
import com.ttubeog.domain.store.dto.response.RegisterStoreRes;
import com.ttubeog.domain.store.dto.response.UpdateStoreRes;
import com.ttubeog.domain.store.exception.UnathorizedMemberException;
import com.ttubeog.domain.store.exception.NonExistentStoreException;
import com.ttubeog.global.config.security.token.UserPrincipal;
import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.global.payload.ApiResponse;
import com.ttubeog.global.payload.Message;
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

    // 매장 수정
    @Transactional
    public ResponseEntity<?> updateStore(UserPrincipal userPrincipal, UpdateStoreReq updateStoreReq) {

        Member member = memberRepository.findById(userPrincipal.getId()).orElseThrow(InvalidMemberException::new);
        Store store = storeRepository.findById(updateStoreReq.getStoreId()).orElseThrow(NonExistentStoreException::new);

        Member storeOwner = store.getMember();
        if (storeOwner.getId() != member.getId()) {
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
    public ResponseEntity<?> deleteStore(UserPrincipal userPrincipal, Long storeId) {

        memberRepository.findById(userPrincipal.getId()).orElseThrow(InvalidMemberException::new);
        Store store = storeRepository.findById(storeId).orElseThrow(NonExistentStoreException::new);
        storeRepository.delete(store);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("매장 정보가 정상적으로 삭제되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
