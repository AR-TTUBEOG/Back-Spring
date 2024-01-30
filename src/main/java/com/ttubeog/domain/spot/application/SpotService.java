package com.ttubeog.domain.spot.application;

import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import com.ttubeog.domain.spot.domain.repository.SpotRepository;
import com.ttubeog.domain.spot.dto.request.CreateSpotRequestDto;
import com.ttubeog.domain.spot.dto.request.UpdateSpotRequestDto;
import com.ttubeog.global.config.security.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SpotService {

    private final SpotRepository spotRepository;
    private final MemberRepository memberRepository;

    public ResponseEntity<?> createSpot(UserPrincipal userPrincipal, CreateSpotRequestDto createSpotRequestDto) {

        memberRepository.findById(userPrincipal.getId()).orElseThrow(InvalidMemberException::new);

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
