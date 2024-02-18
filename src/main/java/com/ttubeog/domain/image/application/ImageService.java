package com.ttubeog.domain.image.application;

import com.ttubeog.domain.auth.security.JwtTokenProvider;
import com.ttubeog.domain.aws.s3.AmazonS3Manager;
import com.ttubeog.domain.guestbook.domain.GuestBook;
import com.ttubeog.domain.guestbook.domain.repository.GuestBookRepository;
import com.ttubeog.domain.guestbook.exception.InvalidGuestBookIdException;
import com.ttubeog.domain.image.domain.Image;
import com.ttubeog.domain.image.domain.ImageType;
import com.ttubeog.domain.image.domain.repository.ImageRepository;
import com.ttubeog.domain.image.dto.request.CreateImageRequestDto;
import com.ttubeog.domain.image.dto.request.UpdateImageRequestDto;
import com.ttubeog.domain.image.dto.response.ImageResponseDto;
import com.ttubeog.domain.image.exception.InvalidImageException;
import com.ttubeog.domain.image.exception.InvalidImageTypeException;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import com.ttubeog.domain.spot.domain.Spot;
import com.ttubeog.domain.spot.domain.repository.SpotRepository;
import com.ttubeog.domain.spot.exception.InvalidSpotIdException;
import com.ttubeog.domain.store.domain.Store;
import com.ttubeog.domain.store.domain.repository.StoreRepository;
import com.ttubeog.domain.store.exception.InvalidStoreIdException;
import com.ttubeog.global.payload.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ImageService {

    private final ImageRepository imageRepository;
    private final SpotRepository spotRepository;
    private final StoreRepository storeRepository;
    private final GuestBookRepository guestBookRepository;
    private final MemberRepository memberRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final AmazonS3Manager amazonS3Manager;


    @Transactional
    public ResponseEntity<?> createSpotImage(HttpServletRequest request, Long spotId, List<MultipartFile> fileList) {

        Long memberId = jwtTokenProvider.getMemberId(request);

        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);

        Spot spot = spotRepository.findById(spotId).orElseThrow(InvalidSpotIdException::new);

        List<ImageResponseDto> imageResponseDtoList = fileList.stream().map(multipartFile -> {
            String uuid = UUID.randomUUID().toString();

            Image image = Image.builder()
                    .uuid(uuid)
                    .imageType(ImageType.SPOT)
                    .spot(spot)
                    .build();

            String imageUrl = amazonS3Manager.uploadFile(amazonS3Manager.generateSpotKeyName(image), multipartFile);
            image.updateImageUrl(imageUrl);
            Image savedImage = imageRepository.save(image);

            return ImageResponseDto.builder()
                    .id(savedImage.getId())
                    .uuid(savedImage.getUuid())
                    .image(savedImage.getImage())
                    .imageType(ImageType.SPOT)
                    .placeId(savedImage.getSpot().getId())
                    .build();
        }).collect(Collectors.toList());


        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(imageResponseDtoList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    @Transactional
    public ResponseEntity<?> createStoreImage(HttpServletRequest request, Long storeId, List<MultipartFile> fileList) {

        Long memberId = jwtTokenProvider.getMemberId(request);

        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);

        Store store = storeRepository.findById(storeId).orElseThrow(InvalidStoreIdException::new);

        List<ImageResponseDto> imageResponseDtoList = fileList.stream().map(multipartFile -> {
            String uuid = UUID.randomUUID().toString();

            Image image = Image.builder()
                    .uuid(uuid)
                    .imageType(ImageType.STORE)
                    .store(store)
                    .build();

            String imageUrl = amazonS3Manager.uploadFile(amazonS3Manager.generateStoreKeyName(image), multipartFile);
            image.updateImageUrl(imageUrl);
            Image savedImage = imageRepository.save(image);

            return ImageResponseDto.builder()
                    .id(savedImage.getId())
                    .uuid(savedImage.getUuid())
                    .image(savedImage.getImage())
                    .imageType(ImageType.STORE)
                    .placeId(savedImage.getStore().getId())
                    .build();
        }).collect(Collectors.toList());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(imageResponseDtoList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    @Transactional
    public ResponseEntity<?> createGuestBookImage(HttpServletRequest request, Long guestBookId, List<MultipartFile> fileList) {

        Long memberId = jwtTokenProvider.getMemberId(request);

        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);

        GuestBook guestBook = guestBookRepository.findById(guestBookId).orElseThrow(InvalidStoreIdException::new);

        List<ImageResponseDto> imageResponseDtoList = fileList.stream().map(multipartFile -> {
            String uuid = UUID.randomUUID().toString();

            Image image = Image.builder()
                    .uuid(uuid)
                    .imageType(ImageType.GUESTBOOK)
                    .guestBook(guestBook)
                    .build();

            String imageUrl = amazonS3Manager.uploadFile(amazonS3Manager.generateGuestBookKeyName(image), multipartFile);
            image.updateImageUrl(imageUrl);
            Image savedImage = imageRepository.save(image);

            return ImageResponseDto.builder()
                    .id(savedImage.getId())
                    .uuid(savedImage.getUuid())
                    .image(savedImage.getImage())
                    .imageType(ImageType.GUESTBOOK)
                    .placeId(savedImage.getGuestBook().getId())
                    .build();
        }).collect(Collectors.toList());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(imageResponseDtoList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    public ResponseEntity<?> findImageBySpotId(HttpServletRequest request, Long spotId) {

        Long memberId = jwtTokenProvider.getMemberId(request);

        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);

        Spot spot = spotRepository.findById(spotId).orElseThrow(InvalidSpotIdException::new);

        List<Image> imageList = imageRepository.findAllBySpot(spot);

        List<ImageResponseDto> imageResponseDtoList = imageList.stream().map(
                image -> ImageResponseDto.builder()
                        .id(image.getId())
                        .uuid(image.getUuid())
                        .image(image.getImage())
                        .imageType(ImageType.SPOT)
                        .placeId(image.getSpot().getId())
                        .build()
        ).toList();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(imageResponseDtoList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    public ResponseEntity<?> findImageByStoreId(HttpServletRequest request, Long storeId) {

        Long memberId = jwtTokenProvider.getMemberId(request);

        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);

        Store store = storeRepository.findById(storeId).orElseThrow(InvalidSpotIdException::new);

        List<Image> imageList = imageRepository.findAllByStore(store);

        List<ImageResponseDto> imageResponseDtoList = imageList.stream().map(
                image -> ImageResponseDto.builder()
                        .id(image.getId())
                        .uuid(image.getUuid())
                        .image(image.getImage())
                        .imageType(ImageType.STORE)
                        .placeId(image.getStore().getId())
                        .build()
        ).toList();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(imageResponseDtoList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    public ResponseEntity<?> findImageByGuestBookId(HttpServletRequest request, Long guestBookId) {

        Long memberId = jwtTokenProvider.getMemberId(request);

        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);

        GuestBook guestBook = guestBookRepository.findById(guestBookId).orElseThrow(InvalidSpotIdException::new);

        List<Image> imageList = imageRepository.findAllByGuestBook(guestBook);

        List<ImageResponseDto> imageResponseDtoList = imageList.stream().map(
                image -> ImageResponseDto.builder()
                        .id(image.getId())
                        .uuid(image.getUuid())
                        .image(image.getImage())
                        .imageType(ImageType.GUESTBOOK)
                        .placeId(image.getGuestBook().getId())
                        .build()
        ).toList();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(imageResponseDtoList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    @Transactional
    public ResponseEntity<?> deleteImageBySpotId(Long spotId) {

        Spot spot = spotRepository.findById(spotId).orElseThrow(InvalidSpotIdException::new);

        List<Image> imageList = imageRepository.findAllBySpot(spot);
        imageRepository.deleteAll(imageList);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information("정상적으로 삭제되었습니다.")
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    @Transactional
    public ResponseEntity<?> deleteImageByStoreId(Long storeId) {

        Store store = storeRepository.findById(storeId).orElseThrow(InvalidStoreIdException::new);

        List<Image> imageList = imageRepository.findAllByStore(store);
        imageRepository.deleteAll(imageList);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information("정상적으로 삭제되었습니다.")
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    @Transactional
    public ResponseEntity<?> deleteImageByGuestBookId(Long guestBookId) {

        GuestBook guestBook = guestBookRepository.findById(guestBookId).orElseThrow(InvalidSpotIdException::new);

        List<Image> imageList = imageRepository.findAllByGuestBook(guestBook);
        imageRepository.deleteAll(imageList);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information("정상적으로 삭제되었습니다.")
                .build();

        return ResponseEntity.ok(apiResponse);
    }

}