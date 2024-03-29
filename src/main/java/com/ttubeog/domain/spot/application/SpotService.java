package com.ttubeog.domain.spot.application;

import com.ttubeog.domain.auth.security.JwtTokenProvider;
import com.ttubeog.domain.guestbook.domain.GuestBook;
import com.ttubeog.domain.guestbook.domain.repository.GuestBookRepository;
import com.ttubeog.domain.image.domain.Image;
import com.ttubeog.domain.image.domain.repository.ImageRepository;
import com.ttubeog.domain.likes.domain.repository.LikesRepository;
import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import com.ttubeog.domain.member.exception.InvalidRegisterMemberException;
import com.ttubeog.domain.spot.domain.Spot;
import com.ttubeog.domain.spot.domain.repository.SpotRepository;
import com.ttubeog.domain.spot.dto.request.CreateSpotRequestDto;
import com.ttubeog.domain.spot.dto.request.UpdateSpotRequestDto;
import com.ttubeog.domain.spot.dto.response.GetSpotDetailRes;
import com.ttubeog.domain.spot.dto.response.SpotResponseDto;
import com.ttubeog.domain.spot.exception.AlreadyExistsSpotException;
import com.ttubeog.domain.spot.exception.InvalidImageListSizeException;
import com.ttubeog.domain.spot.exception.InvalidSpotIdException;
import com.ttubeog.domain.store.exception.NonExistentStoreException;
import com.ttubeog.global.payload.CommonDto;
import com.ttubeog.global.payload.Message;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SpotService {

    private final SpotRepository spotRepository;
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;
    private final GuestBookRepository guestBookRepository;
    private final LikesRepository likesRepository;

    private final JwtTokenProvider jwtTokenProvider;

    @NonNull
    private CommonDto getResponseEntity(Spot spot) {

        SpotResponseDto createSpotResponseDto = SpotResponseDto.builder()
                .id(spot.getId())
                .memberId(spot.getMember().getId())
                .dongAreaId(spot.getDongArea())
                .detailAddress(spot.getDetailAddress())
                .name(spot.getName())
                .info(spot.getInfo())
                .latitude(spot.getLatitude())
                .longitude(spot.getLongitude())
                .stars(spot.getStars())
                .build();

        return new CommonDto(true, createSpotResponseDto);
    }

    //Spot 생성 Method
    @Transactional
    public CommonDto createSpot(HttpServletRequest request, CreateSpotRequestDto createSpotRequestDto) {

        Long memberId = jwtTokenProvider.getMemberId(request);

        // 유효한 사용자 로그인 상태인지 체크
        Member member = memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);

        // 중복된 이름을 가진 산책 스팟인지 체크
        if (spotRepository.findByName(createSpotRequestDto.getName()).isPresent()) {
            throw new AlreadyExistsSpotException();
        }

        // 산책 스팟 이미지가 1~10개 사이인지 체크
        // if (createSpotRequestDto.getImage().isEmpty() || createSpotRequestDto.getImage().size() > 10) {
        //     throw new InvalidImageListSizeException();
        // }
        
        // 산책 스팟 저장
        Spot spot = Spot.builder()
                .member(member)
                .dongArea(createSpotRequestDto.getDongAreaId())
                .detailAddress(createSpotRequestDto.getDetailAddress())
                .name(createSpotRequestDto.getName())
                .info(createSpotRequestDto.getInfo())
                .latitude(createSpotRequestDto.getLatitude())
                .longitude(createSpotRequestDto.getLongitude())
                .stars(0.0f)
                .build();

        spotRepository.save(spot);

        return getResponseEntity(spot);
    }

    //ID로 스팟 조회 Method
    public CommonDto findBySpotId(HttpServletRequest request, Long spotId) {

        Long memberId = jwtTokenProvider.getMemberId(request);
        Member member = memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);
        Spot spot = spotRepository.findById(spotId).orElseThrow(NonExistentStoreException::new);

        Integer guestbookCount = guestBookRepository.countAllBySpot(spot).intValue();
        Integer likesCount = likesRepository.countBySpot(spot);
        Boolean isFavorited = likesRepository.existsByMemberAndSpot(member, spot);

        GetSpotDetailRes getSpotDetailRes = GetSpotDetailRes.builder()
                .spotId(spotId)
                .memberId(spot.getMember().getId())
                .name(spot.getName())
                .info(spot.getInfo())
                .dongAreaId(spot.getDongArea())
                .detailAddress(spot.getDetailAddress())
                .latitude(spot.getLatitude())
                .longitude(spot.getLongitude())
                .stars(spot.getStars())
                .guestbookCount(guestbookCount)
                .likesCount(likesCount)
                .isFavorited(isFavorited)
                .build();

        return new CommonDto(true, getSpotDetailRes);
    }

    @Transactional
    public CommonDto updateSpot(HttpServletRequest request, Long spotId, UpdateSpotRequestDto updateSpotRequestDto) {

        Long memberId = jwtTokenProvider.getMemberId(request);

        // 유효한 사용자 로그인 상태인지 체크
        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);

        // 존재하는 산책 스팟을 수정하려는지 체크
        Spot spot = spotRepository.findById(spotId).orElseThrow(InvalidSpotIdException::new);

        if (spot.getMember().getId().equals(memberId)) {
            throw new InvalidRegisterMemberException("해당 산책 스팟을 등록한 유저가 아닙니다.");
        }

        // 수정하려는 이름과 중복된 이름을 가진 산책 스팟이 있는지
        if (spotRepository.findByName(updateSpotRequestDto.getName()).isPresent()) {
            Spot duplicateSpot =  spotRepository.findByName(updateSpotRequestDto.getName()).orElseThrow(InvalidSpotIdException::new);
            if (!duplicateSpot.getId().equals(spotId)) {
                throw new AlreadyExistsSpotException();
            }
        }

        // 수정하려는 산책 스팟 이미지가 1~10개 사이인지 체크
        if (updateSpotRequestDto.getImage().isEmpty() || updateSpotRequestDto.getImage().size() > 10) {
            throw new InvalidImageListSizeException();
        }

        spot.updateSpot(updateSpotRequestDto.getName(), updateSpotRequestDto.getInfo(), updateSpotRequestDto.getLatitude(), updateSpotRequestDto.getLongitude(), updateSpotRequestDto.getDongAreaId(), updateSpotRequestDto.getDetailAddress());

        spotRepository.save(spot);

        return getResponseEntity(spot);
    }

    //Spot 삭제 Method
    @Transactional
    public CommonDto deleteSpot(HttpServletRequest request, Long spotId) {

        Long memberId = jwtTokenProvider.getMemberId(request);

        // 유효한 사용자 로그인 상태인지 체크
        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);

        Spot spot = spotRepository.findById(spotId).orElseThrow(InvalidSpotIdException::new);

        if (spot.getMember().getId().equals(memberId)) {
            throw new InvalidRegisterMemberException("해당 산책 스팟을 등록한 유저가 아닙니다.");
        }

        spotRepository.delete(spot);

        List<Image> imageList = imageRepository.findBySpotId(spot.getId());
        imageRepository.deleteAll(imageList);

        List<GuestBook> guestBookList = guestBookRepository.findAllBySpot(spot);
        guestBookRepository.deleteAll(guestBookList);

        return new CommonDto(true, Message.builder().message("산책 스팟을 삭제햇습니다.").build());
    }
}
