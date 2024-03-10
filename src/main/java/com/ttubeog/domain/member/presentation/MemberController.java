package com.ttubeog.domain.member.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttubeog.domain.auth.dto.response.OAuthTokenResponse;
import com.ttubeog.domain.member.application.MemberService;
import com.ttubeog.domain.member.dto.request.ProduceNicknameRequest;
import com.ttubeog.domain.member.dto.response.MemberDetailDto;
import com.ttubeog.domain.member.dto.response.MemberNicknameDto;
import com.ttubeog.domain.member.dto.response.MemberPlaceDto;
import com.ttubeog.domain.member.exception.InvalidMemberException;
import com.ttubeog.global.config.security.token.CurrentUser;
import com.ttubeog.global.payload.CommonDto;
import com.ttubeog.global.payload.ErrorResponse;
import com.ttubeog.global.payload.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Tag(name = "Member", description = "Member API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {
    private final MemberService memberService;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    @Operation(summary = "멤버 정보 확인", description = "현재 접속된 멤버 정보를 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "멤버 확인 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MemberDetailDto.class))}),
            @ApiResponse(responseCode = "400", description = "멤버 확인 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("")
    public ResponseEntity<?> getCurrentMember(
            HttpServletRequest request
    ) {

        return memberService.getCurrentUser(request);
    }

    @Operation(summary = "닉네임 설정", description = "현재 접속된 멤버의 초기 닉네임을 설정합니다.닉네임을 이미 변경한 유저는 isChanged == true로 반환되며, 닉네임이 업데이트 되지 않습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "닉네임 설정", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MemberNicknameDto.class))}),
            @ApiResponse(responseCode = "400", description = "닉네임 설정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping(value = "/nickname")
    public ResponseEntity<?> postMemberNickname(
            HttpServletRequest request, @RequestBody ProduceNicknameRequest produceNicknameRequest
    ) {
        return memberService.postMemberNickname(request, produceNicknameRequest);
    }

    @Operation(summary = "닉네임 중복 확인", description = "닉네임의 중복을 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "닉네임 설정 가능", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommonDto.class))}),
            @ApiResponse(responseCode = "400", description = "닉네임 설정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping(value = "/nickname/check")
    public ResponseEntity<?> checkNicknameAvailability(
            HttpServletRequest request, @RequestBody ProduceNicknameRequest produceNicknameRequest
    ) {
        return memberService.postMemberNicknameCheck(request, produceNicknameRequest);
    }


    @Operation(summary = "토큰 재발급", description = "현재 접속된 멤버의 토큰을 재발급 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OAuthTokenResponse.class))}),
            @ApiResponse(responseCode = "400", description = "토큰 재발급 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/login/reissue")
    public ResponseEntity<?> loginReissue(
            HttpServletRequest request
    ) {

        return memberService.getMemberReissueToken(request);
    }

    @Operation(summary = "로그아웃", description = "현재 접속된 멤버가 로그아웃 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "로그아웃 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletRequest request
    ) {
        return memberService.deleteLogout(request);
    }

//    @Operation(summary = "회원탈퇴", description = "현재 접속된 회원이 탈퇴 합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "회원탈퇴 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
//            @ApiResponse(responseCode = "400", description = "회원탈퇴 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
//    })
//    @DeleteMapping("/delete")
//    public ResponseEntity<?> deleteUser(
//            HttpServletRequest request
//    ) {
//        ResponseEntity<?> responseEntity = memberService.deleteUser(request);
//
//        if (responseEntity.getStatusCode().is2xxSuccessful()) {
//            LocalDateTime localDateTime = LocalDateTime.now().plusDays(3);
//
//            scheduledExecutorService.schedule(() -> {
//                ResponseEntity<?> deleteResponse = memberService.deleteInactiveMember();
//
//                if (deleteResponse.getStatusCode().is2xxSuccessful()) {
//                    System.out.println("회원 삭제 성공");
//                } else {
//                    System.out.println("회원 삭제 실패");
//                }
//            }, 3, TimeUnit.DAYS);
//        }
//        return responseEntity;
//    }

    /**
     * 내 산책로 조회 API
     *
     * @param request 멤버 검증
     * @param pageNum
     * @return ResponseEntity -> SpotResponseDto
     * @throws JsonProcessingException
     */
    @Operation(summary = "내 산책 스팟 조회 API",
            description = "내가 등록한 산책 스팟을 조회합니다.\n",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = MemberPlaceDto.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "500 - InvalidMemberException",
                            description = "멤버가 올바르지 않습니다.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = InvalidMemberException.class))
                                    )
                            }
                    ),
            }
    )
    @GetMapping("/spot&{pageNum}")
    public ResponseEntity<?> getMySpotList(
            @CurrentUser HttpServletRequest request,
            @RequestParam(name = "pageNum") Integer pageNum
    ) throws JsonProcessingException {
        return memberService.getMySpotList(request, pageNum);
    }

    /**
     * 내 매장 조회 API
     *
     * @param request 멤버 검증
     * @param pageNum
     * @return ResponseEntity -> SpotResponseDto
     * @throws JsonProcessingException
     */
    @Operation(summary = "내 매장 조회 API",
            description = "내가 등록한 매장을 조회합니다.\n",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = MemberPlaceDto.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "500 - InvalidMemberException",
                            description = "멤버가 올바르지 않습니다.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = InvalidMemberException.class))
                                    )
                            }
                    ),
            }
    )
    @GetMapping("/store&{pageNum}")
    public ResponseEntity<?> getMyStoreList(
            @CurrentUser HttpServletRequest request,
            @RequestParam(name = "pageNum") Integer pageNum
    ) throws JsonProcessingException {
        return memberService.getMyStoreList(request, pageNum);
    }
}
