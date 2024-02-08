package com.ttubeog.domain.guestbook.application;

import com.ttubeog.domain.guestbook.dto.CreateGuestBookRequestDto;
import com.ttubeog.global.config.security.token.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GuestBookService {
    public ResponseEntity<?> createGuestBook(HttpServletRequest request, Integer spotId, CreateGuestBookRequestDto createGuestBookRequestDto) {
        return null;
    }
}
