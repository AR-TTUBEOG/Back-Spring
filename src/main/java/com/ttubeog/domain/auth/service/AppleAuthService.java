package com.ttubeog.domain.auth.service;

import com.ttubeog.domain.auth.feign.AppleAuthClient;
import com.ttubeog.domain.member.application.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppleAuthService {
    private final AppleAuthClient appleAuthClient;
    private final MemberService memberService;
}
