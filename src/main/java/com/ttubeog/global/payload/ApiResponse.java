package com.ttubeog.global.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.ttubeog.global.payload.code.BaseCode;
import com.ttubeog.global.payload.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Data;
import lombok.Builder;


@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
@ToString
@Data
@Builder
public class ApiResponse<T> {

    @JsonProperty("isSuccess")
    @Schema(type = "boolean", example = "true", description = "올바르게 로직을 처리했으면 True, 아니면 False를 반환합니다.")
    private final Boolean isSuccess;
    @Schema(type = "string", example = "code", description = "응답 코드입니다.")
    private final String code;
    @Schema(type = "object", example = "information", description = "restful의 정보를 감싸 표현합니다. object형식으로 표현합니다.")
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;


    // 성공한 경우 응답 생성
    public static <T> ApiResponse<T> onSuccess(T result) {
        return new ApiResponse<>(true, SuccessStatus._OK.getCode(), SuccessStatus._OK.getMessage(), result);
    }

    public static <T> ApiResponse<T> of(BaseCode code, T result) {
        return new ApiResponse<>(true, code.getReasonHttpStatus().getCode(), code.getReasonHttpStatus().getMessage(), result);
    }


    // 실패한 경우 응답 생성
    public static <T> ApiResponse<T> onFailure(String code, String message, T data) {
        return new ApiResponse<>(true, code, message, data);
    }

}
