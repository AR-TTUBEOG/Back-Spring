package com.ttubeog.domain.spot.application;

import com.ttubeog.domain.area.domain.DongArea;
import com.ttubeog.domain.area.domain.repository.DongAreaRepository;
import com.ttubeog.domain.image.application.ImageService;
import com.ttubeog.domain.image.domain.Image;
import com.ttubeog.domain.image.domain.repository.ImageRepository;
import com.ttubeog.domain.image.dto.request.ImageRequestDto;
import com.ttubeog.domain.image.dto.request.ImageRequestType;
import com.ttubeog.domain.image.exception.InvalidImageException;
import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import com.ttubeog.domain.spot.domain.Spot;
import com.ttubeog.domain.spot.domain.repository.SpotRepository;
import com.ttubeog.domain.spot.dto.request.CreateSpotRequestDto;
import com.ttubeog.domain.spot.dto.request.UpdateSpotRequestDto;
import com.ttubeog.domain.spot.dto.response.SpotResponseDto;
import com.ttubeog.domain.spot.exception.AlreadyExistsSpotException;
import com.ttubeog.domain.spot.exception.InvalidDongAreaException;
import com.ttubeog.domain.spot.exception.InvalidImageListSizeException;
import com.ttubeog.domain.spot.exception.InvalidSpotIdException;
import com.ttubeog.global.config.security.token.UserPrincipal;
import com.ttubeog.global.payload.ApiResponse;
import com.ttubeog.global.payload.Message;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.ttubeog.domain.image.application.ImageService.getImageString;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SpotService {

    private final SpotRepository spotRepository;
    private final MemberRepository memberRepository;
    private final DongAreaRepository dongAreaRepository;
    private final ImageRepository imageRepository;

    private final ImageService imageService;

    @NonNull
    private ResponseEntity<?> getResponseEntity(Spot spot) {

        SpotResponseDto createSpotResponseDto = SpotResponseDto.builder()
                .id(spot.getId())
                .memberId(spot.getMember().getId())
                .dongAreaId(spot.getDongArea().getId())
                .detailAddress(spot.getDetailAddress())
                .name(spot.getName())
                .info(spot.getInfo())
                .latitude(spot.getLatitude())
                .longitude(spot.getLongitude())
                .image(getImageString(imageRepository.findBySpotId(spot.getId())))
                .stars(spot.getStars())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(createSpotResponseDto)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<?> createSpot(UserPrincipal userPrincipal, CreateSpotRequestDto createSpotRequestDto) {

        // 유효한 사용자 로그인 상태인지 체크
        Member member = memberRepository.findById(userPrincipal.getId()).orElseThrow(InvalidMemberException::new);

        // 중복된 이름을 가진 산책 스팟인지 체크
        if (spotRepository.findByName(createSpotRequestDto.getName()).isPresent()) {
            throw new AlreadyExistsSpotException();
        }

        // 지역코드가 유효한지 체크
        DongArea dongArea = dongAreaRepository.findById(createSpotRequestDto.getDongAreaId()).orElseThrow(InvalidDongAreaException::new);

        // 산책 스팟 이미지가 1~10개 사이인지 체크
        if (createSpotRequestDto.getImage().isEmpty() || createSpotRequestDto.getImage().size() > 10) {
            throw new InvalidImageListSizeException();
        }
        
        // 산책 스팟 저장
        Spot spot = Spot.builder()
                .member(member)
                .dongArea(dongArea)
                .detailAddress(createSpotRequestDto.getDetailAddress())
                .name(createSpotRequestDto.getName())
                .info(createSpotRequestDto.getInfo())
                .latitude(createSpotRequestDto.getLatitude())
                .longitude(createSpotRequestDto.getLongitude())
                .stars(0.0f)
                .build();

        spotRepository.save(spot);

        // 이미지 저장
        List<String> imageList = createSpotRequestDto.getImage();
        for (String s : imageList) {
            ImageRequestDto imageRequestDto = ImageRequestDto.builder()
                    .image(s)
                    .imageRequestType(ImageRequestType.SPOT)
                    .placeId(spot.getId())
                    .build();
            imageService.createImage(imageRequestDto);
        }

        return getResponseEntity(spot);
    }

    public ResponseEntity<?> findBySpotId(UserPrincipal userPrincipal, Long spotId) {

        // 유효한 사용자 로그인 상태인지 체크
        memberRepository.findById(userPrincipal.getId()).orElseThrow(InvalidMemberException::new);

        Spot spot = spotRepository.findById(spotId).orElseThrow(InvalidSpotIdException::new);

        return getResponseEntity(spot);
    }

    @Transactional
    public ResponseEntity<?> updateSpot(UserPrincipal userPrincipal, UpdateSpotRequestDto updateSpotRequestDto) {

        // 유효한 사용자 로그인 상태인지 체크
        memberRepository.findById(userPrincipal.getId()).orElseThrow(InvalidMemberException::new);

        // 존재하는 산책 스팟을 수정하려는지 체크
        Spot spot = spotRepository.findById(updateSpotRequestDto.getId()).orElseThrow(InvalidSpotIdException::new);

        // 수정하려는 이름과 중복된 이름을 가진 산책 스팟이 있는지
        if (spotRepository.findByName(updateSpotRequestDto.getName()).isPresent()) {
            throw new AlreadyExistsSpotException();
        }

        // 수정하려는 지역코드가 유효한지 체크
        DongArea dongArea = dongAreaRepository.findById(updateSpotRequestDto.getDongAreaId()).orElseThrow(InvalidDongAreaException::new);

        // 수정하려는 산책 스팟 이미지가 1~10개 사이인지 체크
        if (updateSpotRequestDto.getImage().isEmpty() || updateSpotRequestDto.getImage().size() > 10) {
            throw new InvalidImageListSizeException();
        }

        // 이미지 저장
        List<Image> imageList = new ArrayList<>();
        for (int i = 0; i < updateSpotRequestDto.getImage().size(); i++) {
            Image image = Image.builder()
                    .image(updateSpotRequestDto.getImage().get(i))
                    .build();
            imageList.add(imageList.size(), image);
            imageRepository.save(image);
        }

        spot.updateSpot(updateSpotRequestDto.getName(), updateSpotRequestDto.getInfo(), updateSpotRequestDto.getLatitude(), updateSpotRequestDto.getLongitude(), imageList, dongArea, updateSpotRequestDto.getDetailAddress());

        spotRepository.save(spot);

        return getResponseEntity(spot);
    }

    @Transactional
    public ResponseEntity<?> deleteSpot(UserPrincipal userPrincipal, Long spotId) {

        // 유효한 사용자 로그인 상태인지 체크
        Member member = memberRepository.findById(userPrincipal.getId()).orElseThrow(InvalidMemberException::new);

        Spot spot = spotRepository.findById(spotId).orElseThrow(InvalidSpotIdException::new);

        List<Image> imageList = spot.getImages();
        for (Image image : imageList) {
            imageRepository.delete(imageRepository.findById(image.getId()).orElseThrow(InvalidImageException::new));
        }

        spotRepository.delete(spot);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("산책 스팟을 삭제햇습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<?> likeSpot(UserPrincipal userPrincipal, Integer spotId) {
        return null;
    }
}
