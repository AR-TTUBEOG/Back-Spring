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
import com.ttubeog.domain.spot.exception.AlreadyExistsSpotException;
import com.ttubeog.global.config.security.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.font.ImageGraphicAttribute;
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

    public ResponseEntity<?> createSpot(UserPrincipal userPrincipal, CreateSpotRequestDto createSpotRequestDto) {

        memberRepository.findById(userPrincipal.getId()).orElseThrow(InvalidMemberException::new);

        if (spotRepository.findByName(createSpotRequestDto.getName()).isPresent()) {
            throw new AlreadyExistsSpotException();
        }

        Optional<Member> memberOptional = memberRepository.findById(createSpotRequestDto.getMemberId());
        Optional<DongArea> dongAreaOptional = dongAreaRepository.findById(createSpotRequestDto.getDongAreaId());
        List<Image> imageOptional = imageRepository.findBySpotId(createSpotRequestDto.getId());
        if (memberOptional.isPresent() && dongAreaOptional.isPresent() && imageOptional.isPresent()) {
            Member member = memberOptional.get();
            DongArea dongArea = dongAreaOptional.get();
            Image image = imageOptional.get();

            Spot spot = Spot.builder()
                    .member(member)
                    .dongArea(dongArea)
                    .detailAddress(createSpotRequestDto.getDetailAddress())
                    .name(createSpotRequestDto.getName())
                    .info(createSpotRequestDto.getInfo())
                    .latitude(createSpotRequestDto.getLatitude())
                    .longitude(createSpotRequestDto.getLongitude())
                    .images(image)
                    .build();
        }



        return null;
    }

    public ResponseEntity<?> findBySpotId(UserPrincipal userPrincipal, Integer spotId) {
        return null;
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
