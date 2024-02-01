package com.ttubeog.domain.spot.application;

import com.ttubeog.domain.area.domain.DongArea;
import com.ttubeog.domain.area.domain.repository.DongAreaRepository;
import com.ttubeog.domain.image.domain.Image;
import com.ttubeog.domain.image.domain.repository.ImageRepository;
import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import com.ttubeog.domain.spot.domain.Spot;
import com.ttubeog.domain.spot.domain.repository.SpotRepository;
import com.ttubeog.domain.spot.dto.request.CreateSpotRequestDto;
import com.ttubeog.domain.spot.dto.request.UpdateSpotRequestDto;
import com.ttubeog.domain.spot.dto.response.CreateSpotResponseDto;
import com.ttubeog.domain.spot.exception.AlreadyExistsSpotException;
import com.ttubeog.domain.spot.exception.InvalidDongAreaException;
import com.ttubeog.domain.spot.exception.InvalidImageListSizeException;
import com.ttubeog.domain.spot.exception.InvalidSpotIdException;
import com.ttubeog.global.config.security.token.UserPrincipal;
import com.ttubeog.global.payload.ApiResponse;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SpotService {

    private final SpotRepository spotRepository;
    private final MemberRepository memberRepository;
    private final DongAreaRepository dongAreaRepository;
    private final ImageRepository imageRepository;

    private List<String> getSpotImageString(Spot spot) {
        List<String> spotImageString = new ArrayList<>();

        for (int i = 0; i < spot.getImages().size(); i++) {
            spotImageString.add(spotImageString.size(), spot.getImages().get(i).getImage());
        }

        return spotImageString;
    }

    @NotNull
    private ResponseEntity<?> getResponseEntity(Spot spot) {
        CreateSpotResponseDto createSpotResponseDto = CreateSpotResponseDto.builder()
                .id(spot.getId())
                .memberId(spot.getMember().getId())
                .dongAreaId(spot.getDongArea().getId())
                .detailAddress(spot.getDetailAddress())
                .name(spot.getName())
                .info(spot.getInfo())
                .latitude(spot.getLatitude())
                .longitude(spot.getLongitude())
                .image(getSpotImageString(spot))
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
        memberRepository.findById(userPrincipal.getId()).orElseThrow(InvalidMemberException::new);

        // 중복된 이름을 가진 산책 스팟인지 체크
        if (spotRepository.findByName(createSpotRequestDto.getName()).isPresent()) {
            throw new AlreadyExistsSpotException();
        }

        // 산책 스팟 등록 요청 유저가 유효한지 체크
        Member member = memberRepository.findById(createSpotRequestDto.getMemberId()).orElseThrow(InvalidMemberException::new);

        // 지역코드가 유효한지 체크
        DongArea dongArea = dongAreaRepository.findById(createSpotRequestDto.getDongAreaId()).orElseThrow(InvalidDongAreaException::new);

        // 산책 스팟 이미지가 1~10개 사이인지 체크
        if (createSpotRequestDto.getImage().isEmpty() || createSpotRequestDto.getImage().size() > 10) {
            throw new InvalidImageListSizeException();
        }

        // 이미지 저장
        List<Image> imageList = new ArrayList<>();
        for (int i = 0; i < createSpotRequestDto.getImage().size(); i++) {
            Image image = Image.builder()
                    .image(createSpotRequestDto.getImage().get(i))
                    .build();
            imageList.add(imageList.size(), image);
            imageRepository.save(image);
        }

        // 평점 초기화
        Float stars = createSpotRequestDto.getStars() != null ? createSpotRequestDto.getStars() : 0.0f;

        Spot spot = Spot.builder()
                .member(member)
                .dongArea(dongArea)
                .detailAddress(createSpotRequestDto.getDetailAddress())
                .name(createSpotRequestDto.getName())
                .info(createSpotRequestDto.getInfo())
                .latitude(createSpotRequestDto.getLatitude())
                .longitude(createSpotRequestDto.getLongitude())
                .images(imageList)
                .stars(stars)
                .build();

        spotRepository.save(spot);

        return getResponseEntity(spot);
    }

    public ResponseEntity<?> findBySpotId(UserPrincipal userPrincipal, Long spotId) {

        // 유효한 사용자 로그인 상태인지 체크
        memberRepository.findById(userPrincipal.getId()).orElseThrow(InvalidMemberException::new);

        Spot spot = spotRepository.findById(spotId).orElseThrow(InvalidSpotIdException::new);

        return getResponseEntity(spot);
    }

    public ResponseEntity<?> updateSpot(UserPrincipal userPrincipal, UpdateSpotRequestDto updateSpotRequestDto) {
        return null;
    }

    public ResponseEntity<?> deleteSpot(UserPrincipal userPrincipal, Integer spotId) {
        return null;
    }

    public ResponseEntity<?> likeSpot(UserPrincipal userPrincipal, Integer spotId) {
        return null;
    }
}
