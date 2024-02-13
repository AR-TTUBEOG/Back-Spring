package com.ttubeog.domain.member.presentation;

import com.ttubeog.domain.auth.dto.response.OAuthTokenResponse;
import com.ttubeog.domain.member.application.MemberService;
import com.ttubeog.domain.member.dto.request.ProduceNicknameRequest;
import com.ttubeog.domain.member.dto.response.MemberDetailRes;
import com.ttubeog.global.payload.ErrorResponse;
import com.ttubeog.global.payload.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Tag(name = "Member", description = "Member API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {
    private final MemberService memberService;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    @Operation(summary = "멤버 정보 확인", description = "현재 접속된 멤버 정보를 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "멤버 확인 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MemberDetailRes.class))}),
            @ApiResponse(responseCode = "400", description = "멤버 확인 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("")
    public ResponseEntity<?> getCurrentMember(
            HttpServletRequest request
    ) {

        return memberService.getCurrentUser(request);
    }

    @Operation(summary = "닉네임 설정", description = "현재 접속된 멤버의 초기 닉네임을 설정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "닉네임 설정", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MemberDetailRes.class))}),
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
            @ApiResponse(responseCode = "200", description = "닉네임 설정 가능", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MemberDetailRes.class))}),
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

    @Operation(summary = "회원탈퇴", description = "현재 접속된 회원이 탈퇴 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원탈퇴 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "회원탈퇴 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(
            HttpServletRequest request
    ) {
        ResponseEntity<?> responseEntity = memberService.deleteUser(request);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            LocalDateTime localDateTime = LocalDateTime.now().plusDays(3);

            scheduledExecutorService.schedule(() -> {
                ResponseEntity<?> deleteResponse = memberService.deleteInactiveMember();

                if (deleteResponse.getStatusCode().is2xxSuccessful()) {
                    System.out.println("회원 삭제 성공");
                } else {
                    System.out.println("회원 삭제 실패");
                }
            }, 3, TimeUnit.DAYS);
        }
        return responseEntity;
    }
}
