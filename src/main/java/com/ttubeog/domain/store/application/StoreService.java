package com.ttubeog.domain.store.application;

import com.ttubeog.domain.area.domain.DongArea;
import com.ttubeog.domain.area.domain.repository.DongAreaRepository;
import com.ttubeog.domain.auth.security.JwtTokenProvider;
import com.ttubeog.domain.benefit.domain.Benefit;
import com.ttubeog.domain.benefit.domain.BenefitType;
import com.ttubeog.domain.benefit.domain.MemberBenefit;
import com.ttubeog.domain.benefit.domain.repository.BenefitRepository;
import com.ttubeog.domain.benefit.domain.repository.MemberBenefitRepository;
import com.ttubeog.domain.guestbook.domain.GuestBook;
import com.ttubeog.domain.guestbook.domain.repository.GuestBookRepository;
import com.ttubeog.domain.image.application.ImageService;
import com.ttubeog.domain.image.domain.Image;
import com.ttubeog.domain.image.domain.ImageType;
import com.ttubeog.domain.image.domain.repository.ImageRepository;
import com.ttubeog.domain.image.dto.request.CreateImageRequestDto;
import com.ttubeog.domain.likes.domain.Likes;
import com.ttubeog.domain.likes.domain.repository.LikesRepository;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import com.ttubeog.domain.spot.exception.InvalidImageListSizeException;
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

import java.util.List;
import java.util.stream.Collectors;

import static com.ttubeog.domain.image.application.ImageService.getImageString;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;
    private final DongAreaRepository dongAreaRepository;
    private final ImageRepository imageRepository;
    private final BenefitRepository benefitRepository;
    private final MemberBenefitRepository memberBenefitRepository;
    private final GuestBookRepository guestBookRepository;
    private final LikesRepository likesRepository;
    private final ImageService imageService;
    private final JwtTokenProvider jwtTokenProvider;

    // 매장 등록
    @Transactional
    public ResponseEntity<?> registerStore(HttpServletRequest request, RegisterStoreReq registerStoreReq) {

        Long memberId = jwtTokenProvider.getMemberId(request);
        Member member = memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);

        DongArea dongArea = dongAreaRepository.findById(registerStoreReq.getDongAreaId()).orElseThrow(InvalidDongAreaException::new);

        if (registerStoreReq.getImage().isEmpty() || registerStoreReq.getImage().size() > 10) {
            throw new InvalidImageListSizeException();
        }

        Store store = Store.builder()
                .name(registerStoreReq.getName())
                .info(registerStoreReq.getInfo())
                .dongArea(dongArea)
                .member(member)
                .detailAddress(registerStoreReq.getDetailAddress())
                .latitude(registerStoreReq.getLatitude())
                .longitude(registerStoreReq.getLongitude())
                .stars(0.0f)
                .type(registerStoreReq.getType())
                .build();

        storeRepository.save(store);

        // 이미지 저장
        List<String> imageList = registerStoreReq.getImage();
        for (String s : imageList) {
            CreateImageRequestDto createImageRequestDto = CreateImageRequestDto.builder()
                    .image(s)
                    .imageType(ImageType.STORE)
                    .placeId(store.getId())
                    .build();
            imageService.createImage(createImageRequestDto);
        }

        RegisterStoreRes registerStoreRes = RegisterStoreRes.builder()
                .storeId(store.getId())
                .memberId(member.getId())
                .name(store.getName())
                .info(store.getInfo())
                .dongAreaId(store.getDongArea().getId())
                .detailAddress(store.getDetailAddress())
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
                .image(getImageString(imageRepository.findByStoreId(store.getId())))
                .stars(store.getStars())
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

        // 현재 유저가 매장 등록 유저인지 확인
        Long storeOwnerId = store.getMember().getId();
        if (!storeOwnerId.equals(memberId)) {
            throw new UnathorizedMemberException();
        }

        if (updateStoreReq.getImage().isEmpty() || updateStoreReq.getImage().size() > 10) {
            throw new InvalidImageListSizeException();
        }

        store.updateName(updateStoreReq.getName());
        store.updateInfo(updateStoreReq.getInfo());
        store.updateDetailAddress(updateStoreReq.getDetailAddress());
        store.updateLatitude(updateStoreReq.getLatitude());
        store.updateLongitude(updateStoreReq.getLongitude());
        store.updateType(updateStoreReq.getType());

        storeRepository.save(store);

        List<Image> imageList = imageRepository.findByStoreId(store.getId());

        for (Image image : imageList) {
            imageService.deleteImage(image.getId());
        }

        List<String> imageStringList = updateStoreReq.getImage();

        for (String s : imageStringList) {
            CreateImageRequestDto createImageRequestDto = CreateImageRequestDto.builder()
                    .image(s)
                    .imageType(ImageType.STORE)
                    .placeId(store.getId())
                    .build();
            imageService.createImage(createImageRequestDto);
        }

        UpdateStoreRes updateStoreRes = UpdateStoreRes.builder()
                .storeId(store.getId())
                .name(store.getName())
                .info(store.getInfo())
                .detailAddress(store.getDetailAddress())
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
                .image(getImageString(imageRepository.findByStoreId(store.getId())))
                .stars(store.getStars())
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

        // 현재 유저가 매장 등록 유저인지 확인
        Long storeOwnerId = store.getMember().getId();
        if (!storeOwnerId.equals(memberId)) {
            throw new UnathorizedMemberException();
        }

        // 특정 유저가 가진 해당 매장의 혜택 삭제
        List<Benefit> benefitsToDelete = benefitRepository.findByStore(store);
        for (Benefit benefit : benefitsToDelete) {
            List<MemberBenefit> memberBenefits = memberBenefitRepository.findByBenefitId(benefit.getId());
            memberBenefitRepository.deleteAll(memberBenefits);
        }

        // 해당 매장과 연관된 혜택 삭제
        List<Benefit> benefits = benefitRepository.findByStore(store);
        benefitRepository.deleteAll(benefits);

        // 해당 매장과 연관된 방명록 삭제
        List<GuestBook> guestBooks = guestBookRepository.findAllByStore(store);
        guestBookRepository.deleteAll(guestBooks);

        // 해당 매장과 연관된 좋아요 삭제
        List<Likes> likes = likesRepository.findByStoreId(storeId);
        likesRepository.deleteAll(likes);

        // 해당 매장과 연관된 이미지 삭제
        List<Image> images = imageRepository.findByStoreId(storeId);
        imageRepository.deleteAll(images);

        // TODO 해당 매장과 연관된 경로 삭제
        // TODO 해당 매장과 연관된 저장경로 삭제

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

        List<BenefitType> storeBenefits = benefitRepository.findByStore(store)
                .stream()
                .map(Benefit::getType)
                .collect(Collectors.toList());
        Integer guestbookCount = guestBookRepository.countAllByStore(store).intValue();
        Integer likesCount = likesRepository.countByStoreId(storeId);
        //Boolean isFavorited = likesRepository.existsByMemberIdAndStoreId(memberId, storeId);

        GetStoreDetailRes getStoreDetailRes = GetStoreDetailRes.builder()
                .storeId(storeId)
                .memberId(store.getMember().getId())
                .name(store.getName())
                .info(store.getInfo())
                .dongAreaId(store.getDongArea().getId())
                .detailAddress(store.getDetailAddress())
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
                .image(getImageString(imageRepository.findByStoreId(store.getId())))
                .stars(store.getStars())
                .type(store.getType())
                .storeBenefits(storeBenefits)
                .guestbookCount(guestbookCount)
                .likesCount(likesCount)
                //.isFavorited(isFavorited)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(getStoreDetailRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
