package com.ttubeog.domain.user.application;

import com.ttubeog.domain.user.dto.response.UserDetailRes;
import com.ttubeog.domain.user.domain.User;
import com.ttubeog.domain.user.domain.repository.UserRepository;
import com.ttubeog.global.DefaultAssert;
import com.ttubeog.global.config.security.token.UserPrincipal;
import com.ttubeog.global.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    // 현재 유저 조회
    public ResponseEntity<?> getCurrentUser(UserPrincipal userPrincipal){
        Optional<User> checkUser = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isOptionalPresent(checkUser);
        User user = checkUser.get();

        UserDetailRes userDetailRes = UserDetailRes.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .ImgUrl(user.getImageUrl())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(userDetailRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

}
