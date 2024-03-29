package com.ttubeog.domain.likes.application;

import com.ttubeog.domain.auth.security.JwtTokenProvider;
import com.ttubeog.domain.likes.domain.Likes;
import com.ttubeog.domain.likes.domain.repository.LikesRepository;
import com.ttubeog.domain.likes.exception.AlreadyLikesException;
import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import com.ttubeog.domain.spot.domain.Spot;
import com.ttubeog.domain.spot.domain.repository.SpotRepository;
import com.ttubeog.domain.store.domain.Store;
import com.ttubeog.domain.store.domain.repository.StoreRepository;
import com.ttubeog.domain.store.exception.NonExistentStoreException;
import com.ttubeog.domain.spot.exception.NonExistentSpotException;
import com.ttubeog.global.payload.CommonDto;
import com.ttubeog.global.payload.Message;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class LikesService {

    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final SpotRepository spotRepository;
    private final LikesRepository likesRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // 매장 좋아요 누르기
    @Transactional
    public CommonDto likesStore(HttpServletRequest request, Long storeId) {

        Long memberId = jwtTokenProvider.getMemberId(request);
        Member member = memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);
        Store store = storeRepository.findById(storeId).orElseThrow(NonExistentStoreException::new);

        if (likesRepository.existsByMemberAndStore(member, store)) {
            throw new AlreadyLikesException();
        }

        Likes likes = Likes.builder()
                .member(member)
                .store(store)
                .build();

        likesRepository.save(likes);

        return new CommonDto(true, Message.builder().message("매장에 대한 좋아요를 눌렀습니다.").build());
    }

    // 산책스팟 좋아요 누르기
    @Transactional
    public CommonDto likesSpot(HttpServletRequest request, Long spotId) {

        Long memberId = jwtTokenProvider.getMemberId(request);
        Member member = memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);
        Spot spot = spotRepository.findById(spotId).orElseThrow(NonExistentSpotException::new);

        if (likesRepository.existsByMemberAndSpot(member, spot)) {
            throw new AlreadyLikesException();
        }

        Likes likes = Likes.builder()
                .member(member)
                .spot(spot)
                .build();

        likesRepository.save(likes);

        return new CommonDto(true, Message.builder().message("스팟에 대한 좋아요를 눌렀습니다.").build());
    }
}