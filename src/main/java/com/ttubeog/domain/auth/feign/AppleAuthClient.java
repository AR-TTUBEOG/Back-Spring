package com.ttubeog.domain.auth.feign;

import com.ttubeog.domain.auth.dto.apple.ApplePublicKey;
import com.ttubeog.domain.auth.dto.apple.ApplePublicKeyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "appleAuthClient", url = "${apple.auth.public-key-url}")
public interface AppleAuthClient {
    @GetMapping("/keys")
    ApplePublicKeyResponse getAppleAuthPublicKey();
}
