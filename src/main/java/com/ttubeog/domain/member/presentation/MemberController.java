package com.ttubeog.domain.member.presentation;

import com.ttubeog.domain.auth.dto.request.ReissueLoginRequest;
import com.ttubeog.domain.member.application.MemberService;
import com.ttubeog.domain.member.dto.request.ProduceNicknameRequest;
import com.ttubeog.domain.member.dto.response.MemberDetailRes;
import com.ttubeog.global.payload.ErrorResponse;
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

@Tag(name = "Member", description = "Member API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {
    private final MemberService memberService;

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

    @Operation(summary = "토큰 재발급", description = "현재 접속된 멤버의 토큰을 재발급 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MemberDetailRes.class))}),
            @ApiResponse(responseCode = "400", description = "토큰 재발급 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/login/reissue")
    public ResponseEntity<?> loginReissue(
            HttpServletRequest request, @RequestBody ReissueLoginRequest reissueLoginRequest
    ) {

        return null;
    }
}
