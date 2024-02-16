package com.ttubeog.domain.game.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttubeog.domain.auth.security.JwtTokenProvider;
import com.ttubeog.domain.benefit.domain.Benefit;
import com.ttubeog.domain.benefit.domain.MemberBenefit;
import com.ttubeog.domain.benefit.domain.repository.BenefitRepository;
import com.ttubeog.domain.benefit.domain.repository.MemberBenefitRepository;
import com.ttubeog.domain.benefit.exception.NonExistentBenefitException;
import com.ttubeog.domain.game.domain.*;
import com.ttubeog.domain.game.domain.repository.*;
import com.ttubeog.domain.game.dto.request.*;
import com.ttubeog.domain.game.dto.response.*;
import com.ttubeog.domain.game.exception.AlreadyExistGameException;
import com.ttubeog.domain.game.exception.NonExistentGameException;
import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import com.ttubeog.domain.store.domain.Store;
import com.ttubeog.domain.store.domain.repository.StoreRepository;
import com.ttubeog.domain.store.exception.InvalidStoreIdException;
import com.ttubeog.global.payload.ApiResponse;
import com.ttubeog.global.payload.Message;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private final StoreRepository storeRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // 선물게임 생성
    @Transactional
    public ResponseEntity<?> createGift(HttpServletRequest request, CreateGiftReq createGiftReq) throws JsonProcessingException {
        Long memberId = jwtTokenProvider.getMemberId(request);
        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);
        Store store = storeRepository.findById(createGiftReq.getStoreId()).orElseThrow(InvalidStoreIdException::new);

        //store에 이미 존재하는 gametype인지 확인
        List<Benefit> checkBenefitList = benefitRepository.findAllByStoreAndGameIsNotNull(store);
        if (checkBenefitList.stream().anyMatch(benefit -> GameType.GIFT.equals(benefit.getGame().getType()))) {
            throw new AlreadyExistGameException();
        }

        Game game = Game.builder()
                .type(GameType.GIFT)
                .build();

        gameRepository.save(game);

        Benefit benefit = Benefit.builder()
                .store(store)
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
                .storeId(store.getId())
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
    public ResponseEntity<?> createBasketBall(HttpServletRequest request, CreateBasketballReq createBasketballReq) throws JsonProcessingException {
        Long memberId = jwtTokenProvider.getMemberId(request);
        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);
        Store store = storeRepository.findById(createBasketballReq.getStoreId()).orElseThrow(InvalidStoreIdException::new);

        //store에 이미 존재하는 gametype인지 확인
        List<Benefit> checkBenefitList = benefitRepository.findAllByStoreAndGameIsNotNull(store);
        if (checkBenefitList.stream().anyMatch(benefit -> GameType.BASKETBALL.equals(benefit.getGame().getType()))) {
            throw new AlreadyExistGameException();
        }

        Game game = Game.builder()
                .type(GameType.BASKETBALL)
                .build();

        gameRepository.save(game);

        Benefit benefit = Benefit.builder()
                .store(store)
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
                .storeId(store.getId())
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
    public ResponseEntity<?> createRoulette(HttpServletRequest request, CreateRouletteReq createRouletteReq) throws JsonProcessingException {
        Long memberId = jwtTokenProvider.getMemberId(request);
        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);
        Store store = storeRepository.findById(createRouletteReq.getStoreId()).orElseThrow(InvalidStoreIdException::new);

        //store에 이미 존재하는 gametype인지 확인
        List<Benefit> checkBenefitList = benefitRepository.findAllByStoreAndGameIsNotNull(store);
        if (checkBenefitList.stream().anyMatch(benefit -> GameType.ROULETTE.equals(benefit.getGame().getType()))) {
            throw new AlreadyExistGameException();
        }

        Game game = Game.builder()
                .type(GameType.ROULETTE)
                .build();
        gameRepository.save(game);

        List<Benefit> benefitList = createRouletteReq.getOptions().stream()
                .map(option -> Benefit.builder()
                        .store(store)
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

        List<BenefitResDto> benefitResDtoList = benefitList.stream()
                .map(benefit -> BenefitResDto.builder()
                        .benefitId(benefit.getId())
                        .content(benefit.getContent())
                        .type(benefit.getType())
                        .build())
                .collect(Collectors.toList());

        CreateRouletteRes createRouletteRes = CreateRouletteRes.builder()
                .gameId(rouletteGame.getId())
                .storeId(store.getId())
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
    public ResponseEntity<?> updateGift(HttpServletRequest request, UpdateGiftReq updateGiftReq) throws JsonProcessingException {
        Long memberId = jwtTokenProvider.getMemberId(request);
        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);
        GiftGame giftGame = giftGameRepository.findById(updateGiftReq.getGameId()).orElseThrow(NonExistentGameException::new);
        Benefit benefit = benefitRepository.findByGame(giftGame.getGame()).orElseThrow(NonExistentBenefitException::new);
        benefit.deleteGame();
        Store store = storeRepository.findById(updateGiftReq.getStoreId()).orElseThrow(InvalidStoreIdException::new);

        giftGame.updateTimeLimit(updateGiftReq.getTimeLimit());
        giftGame.updateGiftCount(updateGiftReq.getGiftCount());

        Benefit newBenefit = Benefit.builder()
                .store(store)
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
    public ResponseEntity<?> updateBasketball(HttpServletRequest request, UpdateBasketballReq updateBasketballReq) throws JsonProcessingException {
        Long memberId = jwtTokenProvider.getMemberId(request);
        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);
        BasketballGame basketballGame = basketBallRepository.findById(updateBasketballReq.getGameId()).orElseThrow(NonExistentGameException::new);
        Benefit benefit = benefitRepository.findByGame(basketballGame.getGame()).orElseThrow(NonExistentBenefitException::new);
        benefit.deleteGame();
        Store store = storeRepository.findById(updateBasketballReq.getStoreId()).orElseThrow(InvalidStoreIdException::new);

        basketballGame.updateBallCount(updateBasketballReq.getBallCount());
        basketballGame.updateSuccessCount(updateBasketballReq.getSuccessCount());
        basketballGame.updateTimeLimit(updateBasketballReq.getTimeLimit());

        Benefit newBenefit = Benefit.builder()
                .store(store)
                .game(basketballGame.getGame())
                .type(updateBasketballReq.getBenefitType())
                .content(updateBasketballReq.getBenefitContent())
                .build();
        benefitRepository.save(newBenefit);

        UpdateBasketballRes updateBasketballRes = UpdateBasketballRes.builder()
                .gameId(basketballGame.getId())
                .storeId(store.getId())
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
    public ResponseEntity<?> updateRoulette(HttpServletRequest request, UpdateRouletteReq updateRouletteReq) throws JsonProcessingException {
        Long memberId = jwtTokenProvider.getMemberId(request);
        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);
        RouletteGame rouletteGame = rouletteRepository.findById(updateRouletteReq.getGameId()).orElseThrow(NonExistentGameException::new);
        List<Benefit> benefitList = benefitRepository.findAllByGame(rouletteGame.getGame());
        benefitList.forEach(benefit -> benefit.deleteGame());
        Store store = storeRepository.findById(updateRouletteReq.getStoreId()).orElseThrow(InvalidStoreIdException::new);

        List<Benefit> newBenefitList = updateRouletteReq.getOptions().stream()
                .map(option -> Benefit.builder()
                        .store(store)
                        .content(option)
                        .type(updateRouletteReq.getBenefitType())
                        .game(rouletteGame.getGame())
                        .build())
                .collect(Collectors.toList());

        benefitRepository.saveAll(newBenefitList);

        rouletteGame.updateRoulette(updateRouletteReq.getOptions());

        List<BenefitResDto> benefitResDtoList = newBenefitList.stream()
                .map(benefit -> BenefitResDto.builder()
                        .benefitId(benefit.getId())
                        .content(benefit.getContent())
                        .type(benefit.getType())
                        .build())
                .collect(Collectors.toList());

        UpdateRouletteRes updateRouletteRes = UpdateRouletteRes.builder()
                .gameId(rouletteGame.getId())
                .storeId(store.getId())
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
    public ResponseEntity<?> deleteGame(HttpServletRequest request, Long gameId) throws JsonProcessingException {
        Long memberId = jwtTokenProvider.getMemberId(request);
        Member member = memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);
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
    public ResponseEntity<?> findGame(HttpServletRequest request, Long gameId) throws JsonProcessingException {
        Long memberId = jwtTokenProvider.getMemberId(request);
        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);
        Game game = gameRepository.findById(gameId).orElseThrow(NonExistentBenefitException::new);
        List<Benefit> benefitList = benefitRepository.findAllByGame(game);

        List<BenefitResDto> benefitResDtoList = benefitList.stream()
                .filter(benefit -> benefit.getGame() != null && benefit.getGame().getId().equals(game.getId()))
                .map(benefit -> new BenefitResDto(benefit.getId(), benefit.getContent(), benefit.getType()))
                .collect(Collectors.toList());

        FindGameRes.FindGameResBuilder builder = FindGameRes.builder()
                .gameId(game.getId())
                .type(game.getType())
                .benefits(benefitResDtoList);

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
    public ResponseEntity<?> findBenefit(HttpServletRequest request, Long gameId) throws JsonProcessingException {
        Long memberId = jwtTokenProvider.getMemberId(request);
        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);
        Game game = gameRepository.findById(gameId).orElseThrow(NonExistentGameException::new);
        List<Benefit> benefitList = benefitRepository.findAllByGame(game);

        List<BenefitResDto> benefitResDtoList = benefitList.stream()
                .map(benefit -> BenefitResDto.builder()
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

    //매장ID로 게임 조회
    public ResponseEntity<?> findByStore(HttpServletRequest request, Long storeId) throws JsonProcessingException {
        Long memberId = jwtTokenProvider.getMemberId(request);
        memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);
        Store store = storeRepository.findById(storeId).orElseThrow(InvalidStoreIdException::new);
        List<Benefit> benefitList = benefitRepository.findAllByStoreAndGameIsNotNull(store);
        
        List<Game> gameList = benefitList.stream()
                .map(Benefit::getGame)
                .distinct() // 중복 제거
                .collect(Collectors.toList());

        List<FindGameRes> findGameResList = new ArrayList<>();

        for (Game game : gameList) {
            FindGameRes.FindGameResBuilder builder = FindGameRes.builder()
                    .gameId(game.getId())
                    .type(game.getType());

            List<BenefitResDto> benefitResDtoList = benefitList.stream()
                    .filter(benefit -> benefit.getGame() != null && benefit.getGame().getId().equals(game.getId()))
                    .map(benefit -> new BenefitResDto(benefit.getId(), benefit.getContent(), benefit.getType()))
                    .collect(Collectors.toList());

            builder.benefits(benefitResDtoList);

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
            findGameResList.add(findGameRes);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(findGameResList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
