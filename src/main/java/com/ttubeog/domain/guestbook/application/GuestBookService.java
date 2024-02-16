package com.ttubeog.domain.guestbook.application;

import com.ttubeog.domain.auth.security.JwtTokenProvider;
import com.ttubeog.domain.guestbook.domain.GuestBook;
import com.ttubeog.domain.guestbook.domain.GuestBookType;
import com.ttubeog.domain.guestbook.domain.repository.GuestBookRepository;
import com.ttubeog.domain.guestbook.dto.request.CreateGuestBookRequestDto;
import com.ttubeog.domain.guestbook.dto.request.UpdateGuestBookRequestDto;
import com.ttubeog.domain.guestbook.dto.response.GuestBookResponseDto;
import com.ttubeog.domain.guestbook.exception.InvalidGuestBookException;
import com.ttubeog.domain.guestbook.exception.InvalidGuestBookIdException;
import com.ttubeog.domain.image.application.ImageService;
import com.ttubeog.domain.image.domain.Image;
import com.ttubeog.domain.image.domain.ImageType;
import com.ttubeog.domain.image.domain.repository.ImageRepository;
import com.ttubeog.domain.image.dto.request.CreateImageRequestDto;
import com.ttubeog.domain.image.exception.InvalidImageException;
import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import com.ttubeog.domain.spot.domain.Spot;
import com.ttubeog.domain.spot.domain.repository.SpotRepository;
import com.ttubeog.domain.spot.exception.InvalidSpotIdException;
import com.ttubeog.domain.store.domain.Store;
import com.ttubeog.domain.store.domain.repository.StoreRepository;
import com.ttubeog.domain.store.exception.InvalidStoreIdException;
import com.ttubeog.global.payload.ApiResponse;
import com.ttubeog.global.payload.Message;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GuestBookService {

    private final GuestBookRepository guestBookRepository;
    private final MemberRepository memberRepository;
    private final SpotRepository spotRepository;
    private final StoreRepository storeRepository;
    private final ImageRepository imageRepository;

    private final ImageService imageService;

    private final JwtTokenProvider jwtTokenProvider;


    // ResponseEntity 형식에 맞춰 빌드하는 메서드
    @NonNull
    private ResponseEntity<?> getResponseEntity(GuestBook guestBook) {

        GuestBookResponseDto guestBookResponseDto;

        if (guestBook.getGuestBookType().equals(GuestBookType.SPOT)) {
            guestBookResponseDto = GuestBookResponseDto.builder()
                    .id(guestBook.getId())
                    .content(guestBook.getContent())
                    .guestBookType(guestBook.getGuestBookType())
                    .spotId(guestBook.getSpot().getId())
                    .memberId(guestBook.getMember().getId())
                    .star(guestBook.getStar())
                    .build();
        } else if (guestBook.getGuestBookType().equals(GuestBookType.STORE)) {
            guestBookResponseDto = GuestBookResponseDto.builder()
                    .id(guestBook.getId())
                    .content(guestBook.getContent())
                    .guestBookType(guestBook.getGuestBookType())
                    .storeId(guestBook.getStore().getId())
                    .memberId(guestBook.getMember().getId())
                    .star(guestBook.getStar())
                    .build();
        } else {
            throw new InvalidGuestBookException();
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(guestBookResponseDto)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // GuestBook(방명록) 을 하나 생성하는 Service Method 입니다.
    // Spot 이나 Store Controller 혹은 Service 단에서 불러와서 사용 가능합니다.
    @Transactional
    public ResponseEntity<?> createGuestBook(HttpServletRequest request, CreateGuestBookRequestDto createGuestBookRequestDto) {

        Long memberId = jwtTokenProvider.getMemberId(request);

        Member member = memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);

        GuestBook guestBook;

        Spot spot;
        Store store;

        if (createGuestBookRequestDto.getGuestBookType().equals(GuestBookType.SPOT)) {
            spot = spotRepository.findById(createGuestBookRequestDto.getSpotId()).orElseThrow(InvalidSpotIdException::new);

            guestBook = GuestBook.builder()
                    .member(member)
                    .spot(spot)
                    .guestBookType(createGuestBookRequestDto.getGuestBookType())
                    .content(createGuestBookRequestDto.getContent())
                    .star(createGuestBookRequestDto.getStar())
                    .build();
        } else if (createGuestBookRequestDto.getGuestBookType().equals(GuestBookType.STORE)) {
            store = storeRepository.findById(createGuestBookRequestDto.getStoreId()).orElseThrow(InvalidStoreIdException::new);

            guestBook = GuestBook.builder()
                    .member(member)
                    .store(store)
                    .guestBookType(createGuestBookRequestDto.getGuestBookType())
                    .content(createGuestBookRequestDto.getContent())
                    .star(createGuestBookRequestDto.getStar())
                    .build();
        } else {
            throw new InvalidGuestBookException();
        }

        guestBookRepository.save(guestBook);


        CreateImageRequestDto createImageRequestDto = CreateImageRequestDto.builder()
                .image(createGuestBookRequestDto.getImage())
                .imageType(ImageType.GUESTBOOK)
                .placeId(guestBook.getId())
                .build();
        imageService.createImage(createImageRequestDto);


        if (createGuestBookRequestDto.getGuestBookType().equals(GuestBookType.SPOT)) {
            spot = spotRepository.findById(createGuestBookRequestDto.getSpotId()).orElseThrow(InvalidSpotIdException::new);

            Float originStars = guestBookRepository.sumStarBySpotId(spot.getId());

            Long guestBookNum = guestBookRepository.countAllBySpot(spot);

            float updateStarValue;

            updateStarValue = ((originStars + (float)createGuestBookRequestDto.getStar()) / (float)(guestBookNum + 1));

            spot.updateStars(updateStarValue);

            spotRepository.save(spot);
        } else if (createGuestBookRequestDto.getGuestBookType().equals(GuestBookType.STORE)) {
            store = storeRepository.findById(createGuestBookRequestDto.getStoreId()).orElseThrow(InvalidStoreIdException::new);

            Float originStars = guestBookRepository.sumStarByStoreId(store.getId());

            Long guestBookNum = guestBookRepository.countAllByStore(store);

            float updateStarValue;

            updateStarValue = ((originStars + (float)createGuestBookRequestDto.getStar()) / (float)(guestBookNum + 1));

            store.updateStars(updateStarValue);

            storeRepository.save(store);
        } else {
            throw new InvalidGuestBookException();
        }

        return getResponseEntity(guestBook);
    }


    // GuestBook 을 Id(PK) 값으로 찾는 Method 입니다. 현재 사용하지 않습니다.
    public ResponseEntity<?> findByGuestBookId(HttpServletRequest request, Long guestBookId) {
        Long memberId = jwtTokenProvider.getMemberId(request);

        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);

        GuestBook guestBook = guestBookRepository.findById(guestBookId).orElseThrow(InvalidGuestBookIdException::new);

        return getResponseEntity(guestBook);
    }

    // Spot ID 로 GuestBook 을 조회하는 Method 입니다.
    public ResponseEntity<?> findGuestBookBySpotId(HttpServletRequest request, Long spotId, Integer pageNum) {
        Long memberId = jwtTokenProvider.getMemberId(request);

        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);

        Spot spot = spotRepository.findById(spotId).orElseThrow(InvalidSpotIdException::new);

        Page<GuestBook> guestBookPage = guestBookRepository.findAllBySpot(spot, PageRequest.of(pageNum, 10));

        List<GuestBookResponseDto> guestBookResponseDtoList = null;

        for (GuestBook guestBook : guestBookPage) {
            GuestBookResponseDto guestBookResponseDto = GuestBookResponseDto.builder()
                    .id(guestBook.getId())
                    .content(guestBook.getContent())
                    .guestBookType(guestBook.getGuestBookType())
                    .spotId(guestBook.getSpot().getId())
                    .memberId(guestBook.getMember().getId())
                    .star(guestBook.getStar())
                    .build();
            guestBookResponseDtoList.add(guestBookResponseDto);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(guestBookResponseDtoList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Store ID 로 GuestBook 을 조회하는 Method 입니다.
    public ResponseEntity<?> findGuestBookByStoreId(HttpServletRequest request, Long storeId, Integer pageNum) {
        Long memberId = jwtTokenProvider.getMemberId(request);

        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);

        Store store = storeRepository.findById(storeId).orElseThrow(InvalidStoreIdException::new);

        Page<GuestBook> guestBookPage = guestBookRepository.findAllByStore(store, PageRequest.of(pageNum, 10));

        List<GuestBookResponseDto> guestBookResponseDtoList = null;

        for (GuestBook guestBook : guestBookPage) {
            GuestBookResponseDto guestBookResponseDto = GuestBookResponseDto.builder()
                    .id(guestBook.getId())
                    .content(guestBook.getContent())
                    .guestBookType(guestBook.getGuestBookType())
                    .spotId(guestBook.getSpot().getId())
                    .memberId(guestBook.getMember().getId())
                    .star(guestBook.getStar())
                    .build();
            guestBookResponseDtoList.add(guestBookResponseDto);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(guestBookResponseDtoList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    // GuestBook 을 Update 하는 Method 입니다. 현재 따로 사용하지 않습니다.
    @Transactional
    public ResponseEntity<?> updateGuestBook(HttpServletRequest request, Long guestBookId, UpdateGuestBookRequestDto updateGuestBookRequestDto) {

        Long memberId = jwtTokenProvider.getMemberId(request);

        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);

        GuestBook guestBook = guestBookRepository.findById(guestBookId).orElseThrow(InvalidGuestBookIdException::new);

        Image image = imageRepository.findByGuestBookId(guestBook.getId()).orElseThrow(InvalidImageException::new);

        imageService.deleteImage(image.getId());

        CreateImageRequestDto createImageRequestDto = CreateImageRequestDto.builder()
                .image(updateGuestBookRequestDto.getImage())
                .imageType(ImageType.GUESTBOOK)
                .placeId(guestBookId)
                .build();

        Image newImage = imageRepository.findById(imageService.createImage(createImageRequestDto).getId()).orElseThrow(InvalidImageException::new);

        guestBook.updateGuestBook(updateGuestBookRequestDto.getContent(), updateGuestBookRequestDto.getStar(), newImage);

        guestBookRepository.save(guestBook);

        return getResponseEntity(guestBook);
    }

    // GuestBook 을 하나 Delete 하는 Method 입니다.
    @Transactional
    public ResponseEntity<?> deleteGuestBook(HttpServletRequest request, Long guestBookId) {
        Long memberId = jwtTokenProvider.getMemberId(request);

        Member member = memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);

        GuestBook guestBook = guestBookRepository.findById(guestBookId).orElseThrow(InvalidGuestBookIdException::new);

        if (!guestBook.getMember().equals(member)) {
            throw new InvalidMemberException();
        }

        guestBookRepository.delete(guestBook);

        imageService.deleteImage(imageRepository.findByGuestBookId(guestBookId).orElseThrow(InvalidImageException::new).getId());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("방명록을 삭제했습니다").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
