package com.ttubeog.domain.benefit.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttubeog.domain.auth.config.SecurityUtil;
import com.ttubeog.domain.auth.security.JwtTokenProvider;
import com.ttubeog.domain.benefit.domain.Benefit;
import com.ttubeog.domain.benefit.domain.MemberBenefit;
import com.ttubeog.domain.benefit.domain.repository.BenefitRepository;
import com.ttubeog.domain.benefit.domain.repository.MemberBenefitRepository;
import com.ttubeog.domain.benefit.dto.response.SaveBenefitRes;
import com.ttubeog.domain.benefit.exception.AlreadyUsedBenefitException;
import com.ttubeog.domain.benefit.exception.InvalidMemberBenefitException;
import com.ttubeog.domain.benefit.exception.NonExistentBenefitException;
import com.ttubeog.domain.benefit.exception.OverlappingBenefitException;
import com.ttubeog.domain.game.domain.repository.GameRepository;
import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.member.domain.repository.MemberRepository;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import com.ttubeog.domain.store.domain.repository.StoreRepository;
import com.ttubeog.global.config.security.token.UserPrincipal;
import com.ttubeog.global.payload.ApiResponse;
import com.ttubeog.global.payload.Message;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BenefitService {

    private final MemberRepository memberRepository;
    private final BenefitRepository benefitRepository;
    private final StoreRepository storeRepository;
    private final MemberBenefitRepository memberBenefitRepository;
    private final GameRepository gameRepository;
    private final JwtTokenProvider jwtTokenProvider;

    //게임 성공 후 혜택 저장
    @Transactional
    public ResponseEntity<?> saveBenefit(HttpServletRequest request, Long benefitId) throws JsonProcessingException {
        Long memberId = jwtTokenProvider.getMemberId(request);
        Member member = memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);
        Benefit benefit = benefitRepository.findById(benefitId).orElseThrow(NonExistentBenefitException::new);

        //같은 benefit이고, 저장한지 한달이 지나지 않았으면 에러 호출
        if (memberBenefitRepository.existsByBenefitAndCreatedAtIsAfter(benefit, LocalDateTime.now().minusMonths(1))) {
            throw new OverlappingBenefitException();
        }

        MemberBenefit memberBenefit = MemberBenefit.builder()
                .member(member)
                .benefit(benefit)
                .used(false)
                .expired(false)
                .build();

        memberBenefitRepository.save(memberBenefit);

        SaveBenefitRes saveBenefitRes = SaveBenefitRes.builder()
                .id(memberBenefit.getId())
                .benefitId(benefit.getId())
                .content(benefit.getContent())
                .type(benefit.getType())
                .used(memberBenefit.getUsed())
                .expried(memberBenefit.getExpired())
                .createdAt(memberBenefit.getCreatedAt())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(saveBenefitRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    //혜택 사용
    @Transactional
    public ResponseEntity<?> useBenefit(HttpServletRequest request, Long benefitId) throws JsonProcessingException {
        Long memberId = jwtTokenProvider.getMemberId(request);
        Member member = memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);
        Benefit benefit = benefitRepository.findById(benefitId).orElseThrow(NonExistentBenefitException::new);

        //만료기간 안에 혜택은 오직 한개
        MemberBenefit memberBenefit = memberBenefitRepository.findByBenefitAndMemberAndExpiredIsFalse(benefit, member)
                .orElseThrow(InvalidMemberBenefitException::new);

        //이미 사용한 혜택인지 확인
        if (memberBenefit.getUsed()) {
            throw new AlreadyUsedBenefitException();
        }

        memberBenefit.useBenefit();

        SaveBenefitRes saveBenefitRes = SaveBenefitRes.builder()
                .id(memberBenefit.getId())
                .benefitId(benefit.getId())
                .used(memberBenefit.getUsed())
                .expried(memberBenefit.getExpired())
                .createdAt(memberBenefit.getCreatedAt())
                .content(benefit.getContent())
                .type(benefit.getType())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(saveBenefitRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    //혜택 조회(사용 가능, 사용 완료, 만료 혜택 모두 조회)
    public ResponseEntity<?> findMyBenefit(HttpServletRequest request, Integer page) throws JsonProcessingException {
        Long memberId = jwtTokenProvider.getMemberId(request);
        Member member = memberRepository.findById(memberId).orElseThrow(InvalidMemberException::new);

        Page<MemberBenefit> memberBenefitPage = memberBenefitRepository.findAllByMember(member, PageRequest.of(page, 10));

        List<SaveBenefitRes> saveBenefitRes = memberBenefitPage.stream().map(
                memberBenefit -> SaveBenefitRes.builder()
                        .id(memberBenefit.getId())
                        .benefitId(memberBenefit.getBenefit().getId())
                        .used(memberBenefit.getUsed())
                        .expried(memberBenefit.getExpired())
                        .createdAt(memberBenefit.getCreatedAt())
                        .content(memberBenefit.getBenefit().getContent())
                        .type(memberBenefit.getBenefit().getType())
                        .build()
        ).toList();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(saveBenefitRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    //한달지나면 expired true로 만들기
    @Transactional
    @Scheduled(fixedRate = 60*60000) //1시간마다 검사
    public void autoCheckExpired() {
        System.out.println("혜택 유효기간 체크");
        List<MemberBenefit> memberBenefitList = memberBenefitRepository.findAllByExpiredFalse();

        if (!memberBenefitList.isEmpty()) {
            for (MemberBenefit memberBenefit : memberBenefitList) {
                if (memberBenefit.getCreatedAt().isBefore(LocalDateTime.now().minusMonths(1))) {
                    memberBenefit.terminateBenefit();
                }
            }
        }
    }
}
