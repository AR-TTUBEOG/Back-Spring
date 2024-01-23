package com.ttubeog.domain.auth.utils;

import org.springframework.stereotype.Component;

@Component
public class AppleJwtParser {
    private static final String IDENTITY_TOKEN_VALUE_DELIMITER = "\\.";
    private static final int HEADER_INDEX = 0;
}
