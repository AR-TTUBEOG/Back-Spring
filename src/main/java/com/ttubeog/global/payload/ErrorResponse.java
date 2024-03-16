package com.ttubeog.global.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class ErrorResponse<T> {

    @Schema(description = "에러가 발생한 데이터")
    private final Map<String, T> data;

    @Schema(description = "에러 코드 [{알파벳}-{숫자}] 형태", example = "E-001")
    private final String code;

    @Schema(description = "에러 메세지", example = "잘못된 요청입니다.")
    private final String message;

    private ErrorResponse(Map<String, T> data, String code, String message) {
        this.data = data;
        this.code = code;
        this.message = message;
    }

    public static ErrorResponse<Object> error(String code, String message) {
        return error(Map.of(), code, message);
    }

    public static <T> ErrorResponse<T> error(Map<String, T> data, String code, String message) {
        return new ErrorResponse<>(data, code, message);
    }

}
