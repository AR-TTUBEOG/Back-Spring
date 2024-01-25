package com.ttubeog.domain.game.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttubeog.domain.benefit.domain.Benefit;
import com.ttubeog.domain.benefit.domain.repository.BenefitRepository;
import com.ttubeog.domain.benefit.exception.NonExistentBenefitException;
import com.ttubeog.domain.game.domain.*;
import com.ttubeog.domain.game.domain.repository.*;
import com.ttubeog.domain.game.dto.request.CreateBasketballReq;
import com.ttubeog.domain.game.dto.request.CreateGiftReq;
import com.ttubeog.domain.game.dto.request.CreateRouletteReq;
import com.ttubeog.domain.game.dto.request.UpdateGiftReq;
import com.ttubeog.domain.game.dto.response.CreateBasketballRes;
import com.ttubeog.domain.game.dto.response.CreateGiftRes;
import com.ttubeog.domain.game.dto.response.CreateRouletteRes;
import com.ttubeog.domain.game.dto.response.UpdateGiftRes;
import com.ttubeog.domain.game.exception.NonExistentException;
import com.ttubeog.domain.game.exception.OverlappingGameException;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import com.ttubeog.global.config.security.token.UserPrincipal;
import com.ttubeog.global.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

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
    private final RouletteOptionRepository rouletteOptionRepository;

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
                .optionCount(createRouletteReq.getOption())
                .build();
        rouletteRepository.save(rouletteGame);

        List<String> contents = Arrays.asList(createRouletteReq.getContents());
        List<RouletteOption> rouletteOptionList = contents.stream().map(
                content -> RouletteOption.builder()
                        .content(content)
                        .rouletteGame(rouletteGame)
                        .build()
        ).toList();
        rouletteOptionRepository.saveAll(rouletteOptionList);

        CreateRouletteRes createRouletteRes = CreateRouletteRes.builder()
                .gameId(rouletteGame.getId())
                .benefitId(benefit.getId())
                .option(rouletteGame.getOptionCount())
                .contents(contents)
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
        GiftGame giftGame = giftGameRepository.findById(updateGiftReq.getGameId()).orElseThrow(NonExistentException::new);

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
}
