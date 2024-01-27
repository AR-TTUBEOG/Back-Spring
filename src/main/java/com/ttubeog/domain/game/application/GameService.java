package com.ttubeog.domain.game.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttubeog.domain.benefit.domain.Benefit;
import com.ttubeog.domain.benefit.domain.repository.BenefitRepository;
import com.ttubeog.domain.benefit.exception.NonExistentBenefitException;
import com.ttubeog.domain.game.domain.*;
import com.ttubeog.domain.game.domain.repository.*;
import com.ttubeog.domain.game.dto.request.*;
import com.ttubeog.domain.game.dto.response.*;
import com.ttubeog.domain.game.exception.NonExistentGameException;
import com.ttubeog.domain.game.exception.OverlappingGameException;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import com.ttubeog.global.config.security.token.UserPrincipal;
import com.ttubeog.global.payload.ApiResponse;
import com.ttubeog.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
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
    private final BasketBallRepository basketBallRepository;
    private final RouletteRepository rouletteRepository;

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

        basketBallRepository.save(basketballGame);

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

    //돌림판 게임 생성
    @Transactional
    public ResponseEntity<?> createRoulette(UserPrincipal userPrincipal, CreateRouletteReq createRouletteReq) throws JsonProcessingException {

        memberRepository.findById(userPrincipal.getId()).orElseThrow(InvalidMemberException::new);
        Benefit benefit = benefitRepository.findById(createRouletteReq.getBenefitId()
        ).orElseThrow(NonExistentBenefitException::new);

        //하나의 혜택에 같은 종류 Game이 들어갈 수 없음
        if (gameRepository.existsByBenefitAndType(benefit, GameType.roulette)) {
            throw new OverlappingGameException();
        }

        Game game = Game.builder()
                .benefit(benefit)
                .type(GameType.roulette)
                .build();
        gameRepository.save(game);

        RouletteGame rouletteGame = RouletteGame.builder()
                .game(game)
                .options(createRouletteReq.getOptions())
                .build();
        rouletteRepository.save(rouletteGame);

        CreateRouletteRes createRouletteRes = CreateRouletteRes.builder()
                .gameId(rouletteGame.getId())
                .benefitId(benefit.getId())
                .options(rouletteGame.getOptions())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(createRouletteRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    //선물게임 수정
    @Transactional
    public ResponseEntity<?> updateGift(UserPrincipal userPrincipal, UpdateGiftReq updateGiftReq) throws JsonProcessingException {

        memberRepository.findById(userPrincipal.getId()).orElseThrow(InvalidMemberException::new);
        GiftGame giftGame = giftGameRepository.findById(updateGiftReq.getGameId()).orElseThrow(NonExistentGameException::new);

        giftGame.updateTimeLimit(updateGiftReq.getTimeLimit());
        giftGame.updateGiftCount(updateGiftReq.getGiftCount());

        UpdateGiftRes updateGiftRes = UpdateGiftRes.builder()
                .gameId(giftGame.getId())
                .giftCount(giftGame.getGiftCount())
                .timeLimit(giftGame.getTimeLimit())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(updateGiftRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    //농구게임 수정
    @Transactional
    public ResponseEntity<?> updateBasketball(UserPrincipal userPrincipal, UpdateBasketballReq updateBasketballReq) throws JsonProcessingException {

        memberRepository.findById(userPrincipal.getId()).orElseThrow(InvalidMemberException::new);
        BasketballGame basketballGame = basketBallRepository.findById(updateBasketballReq.getGameId()).orElseThrow(NonExistentGameException::new);

        basketballGame.updateBallCount(updateBasketballReq.getBallCount());
        basketballGame.updateSuccessCount(updateBasketballReq.getSuccessCount());
        basketballGame.updateTimeLimit(updateBasketballReq.getTimeLimit());

        UpdateBasketballRes updateBasketballRes = UpdateBasketballRes.builder()
                .gameId(basketballGame.getId())
                .ballCount(basketballGame.getBallCount())
                .timeLimit(basketballGame.getTimeLimit())
                .successCount(basketballGame.getSuccessCount())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(updateBasketballRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    //돌림판 게임 수정
    @Transactional
    public ResponseEntity<?> updateRoulette(UserPrincipal userPrincipal, UpdateRouletteReq updateRouletteReq) throws JsonProcessingException {

        memberRepository.findById(userPrincipal.getId()).orElseThrow(InvalidMemberException::new);
        RouletteGame rouletteGame = rouletteRepository.findById(updateRouletteReq.getGameId()).orElseThrow(NonExistentGameException::new);

        rouletteGame.updateOptions(updateRouletteReq.getOptions());

        UpdateRouletteRes updateRouletteRes = UpdateRouletteRes.builder()
                .gameId(rouletteGame.getId())
                .options(rouletteGame.getOptions())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(updateRouletteRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    //게임 삭제
    @Transactional
    public ResponseEntity<?> deleteGame(UserPrincipal userPrincipal, Long gameId) throws JsonProcessingException {

        memberRepository.findById(userPrincipal.getId()).orElseThrow(InvalidMemberException::new);
        Game game = gameRepository.findById(gameId).orElseThrow(NonExistentBenefitException::new);

        gameRepository.delete(game);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("게임을 삭제했습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    //게임 조회
    @Transactional
    public ResponseEntity<?> findGame(UserPrincipal userPrincipal, Long gameId) throws JsonProcessingException {

        memberRepository.findById(userPrincipal.getId()).orElseThrow(InvalidMemberException::new);
        Game game = gameRepository.findById(gameId).orElseThrow(NonExistentBenefitException::new);

        FindGameRes.FindGameResBuilder builder = FindGameRes.builder()
                .gameId(game.getId())
                .type(game.getType());

        if (game.getType() == GameType.basketball) {
            builder.timeLimit(game.getBasketballGame().getTimeLimit())
                    .ballCount(game.getBasketballGame().getBallCount())
                    .successCount(game.getBasketballGame().getSuccessCount());
        } else if (game.getType() == GameType.gift) {
            builder.timeLimit(game.getGiftGame().getTimeLimit())
                    .giftCount(game.getGiftGame().getGiftCount());
        } else if (game.getType() == GameType.roulette) {
            Hibernate.initialize(game.getRouletteGame().getOptions()); // 명시적 초기화
            builder.options(game.getRouletteGame().getOptions());
        }

        FindGameRes findGameRes = builder.build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(findGameRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

}
