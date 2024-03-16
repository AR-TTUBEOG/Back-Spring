package com.ttubeog.global.payload.response;

import java.time.LocalDateTime;
import java.util.List;

//import com.ecolink.core.common.dto.CursorPage;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ApiCursorPageResponse<T, C> {

    @Schema(description = "데이터")
    private final List<T> data;
    @Schema(description = "첫 데이터의 커서")
    private final C first;
    @Schema(description = "마지막 데이터의 커서")
    private final C end;
    @Schema(description = "다음 조회할 데이터의 첫 커서")
    private final C next;
    @Schema(description = "다음 데이터 존재 여부")
    private final boolean hasNext;
    @Schema(description = "비어있는 지 여부", example = "false")
    private final boolean empty;
    @Schema(description = "페이지 사이즈", example = "16")
    private final Integer size;
    @Schema(description = "조회 기준 시각")
    private final LocalDateTime referenceTime;
    @Schema(description = "응답 코드", example = "200")
    private final int code;
    @Schema(description = "응답 메세지", example = "success")
    private final String message;
/*
    private ApiCursorPageResponse(CursorPage<T, C> data, int code, String message) {
        this.data = data.getData();
        this.first = data.getFirst();
        this.end = data.getEnd();
        this.next = data.getNext();
        this.hasNext = data.isHasNext();
        this.empty = data.isEmpty();
        this.size = data.getSize();
        this.referenceTime = data.getReferenceTime();
        this.code = code;
        this.message = message;
    }

    public static <T, C> ApiCursorPageResponse<T, C> ok(CursorPage<T, C> data) {
        return new ApiCursorPageResponse<>(data, 200, "OK");
    }

 */
}