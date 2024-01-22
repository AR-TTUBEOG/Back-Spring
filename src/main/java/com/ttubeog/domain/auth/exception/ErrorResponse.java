package com.ttubeog.domain.auth.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private String errMsg;

    public String convertToJson() throws JsonProcessingException {
        return objectMapper.writeValueAsString(this);
    }
}
