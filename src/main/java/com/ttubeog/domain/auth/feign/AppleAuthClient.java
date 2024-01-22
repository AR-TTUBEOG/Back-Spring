package com.ttubeog.domain.auth.feign;

import com.ttubeog.domain.auth.dto.apple.ApplePublicKey;
import com.ttubeog.domain.auth.dto.apple.ApplePublicKeyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "appleAuthClient", url = "https://appleid.apple.com/auth")
public interface AppleAuthClient {
    @GetMapping
    ApplePublicKey getAppleAuthPublicKey();
}
