package com.ttubeog.domain.game.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttubeog.domain.benefit.domain.Benefit;
import com.ttubeog.domain.benefit.domain.MemberBenefit;
import com.ttubeog.domain.benefit.domain.repository.BenefitRepository;
import com.ttubeog.domain.benefit.domain.repository.MemberBenefitRepository;
import com.ttubeog.domain.benefit.exception.NonExistentBenefitException;
import com.ttubeog.domain.game.domain.*;
import com.ttubeog.domain.game.domain.repository.*;
import com.ttubeog.domain.game.dto.request.*;
import com.ttubeog.domain.game.dto.response.*;
import com.ttubeog.domain.game.exception.NonExistentGameException;
import com.ttubeog.domain.member.domain.Member;
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

import java.util.List;
import java.util.stream.Collectors;

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
    private final MemberBenefitRepository memberBenefitRepository;

    // 선물게임 생성
    @Transactional
    public ResponseEntity<?> createGift(UserPrincipal userPrincipal, CreateGiftReq createGiftReq) throws JsonProcessingException {

        memberRepository.findById(userPrincipal.getId()).orElseThrow(InvalidMemberException::new);

        //TODO 연결 후 store에 이미 존재하는 gametype인지 확인

        Game game = Game.builder()
                .type(GameType.GIFT)
                .build();

        gameRepository.save(game);

        Benefit benefit = Benefit.builder()
                .content(createGiftReq.getBenefitContent())
                .type(createGiftReq.getBenefitType())
                .game(game)
                .build();

        benefitRepository.save(benefit);

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
                .benefitType(benefit.getType())
                .benefitContent(benefit.getContent())
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

        Game game = Game.builder()
                .type(GameType.BASKETBALL)
                .build();

        gameRepository.save(game);

        Benefit benefit = Benefit.builder()
                .content(createBasketballReq.getBenefitContent())
                .type(createBasketballReq.getBenefitType())
                .game(game)
                .build();

        benefitRepository.save(benefit);

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
                .benefitType(benefit.getType())
                .benefitContent(benefit.getContent())
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

        Game game = Game.builder()
                .type(GameType.ROULETTE)
                .build();
        gameRepository.save(game);

        List<Benefit> benefitList = createRouletteReq.getOptions().stream()
                .map(option -> Benefit.builder()
                        .content(option)
                        .type(createRouletteReq.getBenefitType())
                        .game(game)
                        .build())
                .collect(Collectors.toList());

        benefitRepository.saveAll(benefitList);

        RouletteGame rouletteGame = RouletteGame.builder()
                .game(game)
                .options(createRouletteReq.getOptions())
                .build();
        rouletteRepository.save(rouletteGame);

        List<RouletteBenefitResDto> benefitResDtoList = benefitList.stream()
                .map(benefit -> RouletteBenefitResDto.builder()
                        .benefitId(benefit.getId())
                        .content(benefit.getContent())
                        .type(benefit.getType())
                        .build())
                .collect(Collectors.toList());

        CreateRouletteRes createRouletteRes = CreateRouletteRes.builder()
                .gameId(rouletteGame.getId())
                .benefits(benefitResDtoList)
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
        Benefit benefit = benefitRepository.findByGame(giftGame.getGame()).orElseThrow(NonExistentBenefitException::new);
        benefit.deleteGame();

        giftGame.updateTimeLimit(updateGiftReq.getTimeLimit());
        giftGame.updateGiftCount(updateGiftReq.getGiftCount());

        Benefit newBenefit = Benefit.builder()
                .game(giftGame.getGame())
                .type(updateGiftReq.getBenefitType())
                .content(updateGiftReq.getBenefitContent())
                .build();
        benefitRepository.save(newBenefit);

        UpdateGiftRes updateGiftRes = UpdateGiftRes.builder()
                .gameId(giftGame.getId())
                .giftCount(giftGame.getGiftCount())
                .timeLimit(giftGame.getTimeLimit())
                .benefitId(newBenefit.getId())
                .benefitContent(newBenefit.getContent())
                .benefitType(newBenefit.getType())
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
        Benefit benefit = benefitRepository.findByGame(basketballGame.getGame()).orElseThrow(NonExistentBenefitException::new);
        benefit.deleteGame();

        basketballGame.updateBallCount(updateBasketballReq.getBallCount());
        basketballGame.updateSuccessCount(updateBasketballReq.getSuccessCount());
        basketballGame.updateTimeLimit(updateBasketballReq.getTimeLimit());

        Benefit newBenefit = Benefit.builder()
                .game(basketballGame.getGame())
                .type(updateBasketballReq.getBenefitType())
                .content(updateBasketballReq.getBenefitContent())
                .build();
        benefitRepository.save(newBenefit);

        UpdateBasketballRes updateBasketballRes = UpdateBasketballRes.builder()
                .gameId(basketballGame.getId())
                .ballCount(basketballGame.getBallCount())
                .timeLimit(basketballGame.getTimeLimit())
                .successCount(basketballGame.getSuccessCount())
                .benefitId(newBenefit.getId())
                .benefitContent(newBenefit.getContent())
                .benefitType(newBenefit.getType())
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
        List<Benefit> benefitList = benefitRepository.findAllByGame(rouletteGame.getGame());
        benefitList.forEach(benefit -> benefit.deleteGame());

        List<Benefit> newBenefitList = updateRouletteReq.getOptions().stream()
                .map(option -> Benefit.builder()
                        .content(option)
                        .type(updateRouletteReq.getBenefitType())
                        .game(rouletteGame.getGame())
                        .build())
                .collect(Collectors.toList());

        benefitRepository.saveAll(newBenefitList);

        rouletteGame.updateRoulette(updateRouletteReq.getOptions());

        List<RouletteBenefitResDto> benefitResDtoList = newBenefitList.stream()
                .map(benefit -> RouletteBenefitResDto.builder()
                        .benefitId(benefit.getId())
                        .content(benefit.getContent())
                        .type(benefit.getType())
                        .build())
                .collect(Collectors.toList());

        UpdateRouletteRes updateRouletteRes = UpdateRouletteRes.builder()
                .gameId(rouletteGame.getId())
                .options(rouletteGame.getOptions())
                .benefits(benefitResDtoList)
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

        Member member = memberRepository.findById(userPrincipal.getId()).orElseThrow(InvalidMemberException::new);
        Game game = gameRepository.findById(gameId).orElseThrow(NonExistentGameException::new);
        List<Benefit> benefitList = benefitRepository.findAllByGame(game);
        List<MemberBenefit> memberBenefitList = memberBenefitRepository.findAllByMemberAndBenefitIn(member, benefitList);

        memberBenefitRepository.deleteAll(memberBenefitList);
        benefitRepository.deleteAll(benefitList);
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

        if (game.getType() == GameType.BASKETBALL) {
            builder.timeLimit(game.getBasketballGame().getTimeLimit())
                    .ballCount(game.getBasketballGame().getBallCount())
                    .successCount(game.getBasketballGame().getSuccessCount());
        } else if (game.getType() == GameType.GIFT) {
            builder.timeLimit(game.getGiftGame().getTimeLimit())
                    .giftCount(game.getGiftGame().getGiftCount());
        } else if (game.getType() == GameType.ROULETTE) {
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

    //게임ID로 혜택 조회
    @Transactional
    public ResponseEntity<?> findBenefit(UserPrincipal userPrincipal, Long gameId) throws JsonProcessingException {

        memberRepository.findById(userPrincipal.getId()).orElseThrow(InvalidMemberException::new);
        Game game = gameRepository.findById(gameId).orElseThrow(NonExistentGameException::new);
        List<Benefit> benefitList = benefitRepository.findAllByGame(game);

        List<RouletteBenefitResDto> benefitResDtoList = benefitList.stream()
                .map(benefit -> RouletteBenefitResDto.builder()
                        .benefitId(benefit.getId())
                        .content(benefit.getContent())
                        .type(benefit.getType())
                        .build())
                .collect(Collectors.toList());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(benefitResDtoList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

}
