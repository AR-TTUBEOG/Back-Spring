package com.ttubeog.global.payload.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ApiResponse<T> {

    @Schema(description = "요청한 응답에 대한 데이터")
    private final T data;

    @Schema(description = "HTTP 상태 코드", example = "200")
    private final int status;

    @Schema(description = "요청 관련 메세지", example = "OK")
    private final String message;

    public ApiResponse(T data, int status, String message) {
        this.data = data;
        this.status = status;
        this.message = message;
    }

    public static ApiResponse<Void> ok() {
        return ok(null);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(data, 200, "OK");
    }

    public static<T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(data, 201, "Created");
    }
}
