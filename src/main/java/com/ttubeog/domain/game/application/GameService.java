package com.ttubeog.domain.game.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttubeog.domain.benefit.domain.Benefit;
import com.ttubeog.domain.benefit.domain.repository.BenefitRepository;
import com.ttubeog.domain.benefit.exception.NonExistentBenefitException;
import com.ttubeog.domain.game.domain.BasketballGame;
import com.ttubeog.domain.game.domain.Game;
import com.ttubeog.domain.game.domain.GameType;
import com.ttubeog.domain.game.domain.GiftGame;
import com.ttubeog.domain.game.domain.repository.BasketBallRespository;
import com.ttubeog.domain.game.domain.repository.GameRepository;
import com.ttubeog.domain.game.domain.repository.GiftGameRepository;
import com.ttubeog.domain.game.dto.request.CreateBasketballReq;
import com.ttubeog.domain.game.dto.request.CreateGiftReq;
import com.ttubeog.domain.game.dto.response.CreateBasketballRes;
import com.ttubeog.domain.game.dto.response.CreateGiftRes;
import com.ttubeog.domain.game.exception.OverlappingGameException;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import com.ttubeog.global.config.security.token.UserPrincipal;
import com.ttubeog.global.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GameService {

    private final GameRepository gameRepository;
    private final MemberRepository memberRepository;
    private final BenefitRepository benefitRepository;
    private final GiftGameRepository giftGameRepository;
    private final BasketBallRespository basketBallRespository;

    // 선물게임 생성
    @Transactional
    public ResponseEntity<?> createGift(UserPrincipal userPrincipal, CreateGiftReq createGiftReq) throws JsonProcessingException {

        memberRepository.findById(userPrincipal.getId()).orElseThrow(InvalidMemberException::new);
        Benefit benefit = benefitRepository.findById(createGiftReq.getBenefitId()
        ).orElseThrow(NonExistentBenefitException::new);

        //하나의 혜택에 같은 종류 Game이 들어갈 수 없음
        if (gameRepository.existsByBenefitAndType(benefit, GameType.gift)) {
            throw new OverlappingGameException();
        }

        Game game = Game.builder()
                .benefit(benefit)
                .type(GameType.gift)
                .build();

        gameRepository.save(game);

        GiftGame giftGame = GiftGame.builder()
                .game(game)
                .timeLimit(createGiftReq.getTimeLimit())
                .giftCount(createGiftReq.getGiftCount())
                .build();

        giftGameRepository.save(giftGame);

        CreateGiftRes createGiftRes = CreateGiftRes.builder()
                .gameId(game.getId())
                .benefitId(benefit.getId())
                .giftCount(giftGame.getGiftCount())
                .timeLimit(giftGame.getTimeLimit())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(createGiftRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 농구게임 생성
    @Transactional
    public ResponseEntity<?> createBasketBall(UserPrincipal userPrincipal, CreateBasketballReq createBasketballReq) throws JsonProcessingException {

        memberRepository.findById(userPrincipal.getId()).orElseThrow(InvalidMemberException::new);
        Benefit benefit = benefitRepository.findById(createBasketballReq.getBenefitId()
        ).orElseThrow(NonExistentBenefitException::new);

        //하나의 혜택에 같은 종류 Game이 들어갈 수 없음
        if (gameRepository.existsByBenefitAndType(benefit, GameType.basketball)) {
            throw new OverlappingGameException();
        }

        Game game = Game.builder()
                .benefit(benefit)
                .type(GameType.basketball)
                .build();

        gameRepository.save(game);

        BasketballGame basketballGame = BasketballGame.builder()
                .game(game)
                .timeLimit(createBasketballReq.getTimeLimit())
                .ballCount(createBasketballReq.getBallCount())
                .successCount(createBasketballReq.getSuccessCount())
                .build();

        basketBallRespository.save(basketballGame);

        CreateBasketballRes createBasketballRes = CreateBasketballRes.builder()
                .gameId(game.getId())
                .benefitId(benefit.getId())
                .timeLimit(basketballGame.getTimeLimit())
                .ballCount(basketballGame.getBallCount())
                .successCount(basketballGame.getSuccessCount())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(createBasketballRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
