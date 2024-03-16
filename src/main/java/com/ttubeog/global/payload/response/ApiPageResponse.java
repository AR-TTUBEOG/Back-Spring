package com.ttubeog.global.payload.response;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ApiPageResponse<T> {

    @Schema(description = "데이터")
    private final List<T> data;
    @Schema(description = "비어있는 지 여부", example = "false")
    private final boolean empty;
    @Schema(description = "첫 페이지 여부", example = "true")
    private final boolean first;
    @Schema(description = "마지막 페이지 여부", example = "false")
    private final boolean last;
    @Schema(description = "검색 전체 개수", example = "32")
    private final long totalElements;
    @Schema(description = "현재 페이지", example = "1")
    private final int pageNumber;
    @Schema(description = "페이지 사이즈", example = "10")
    private final int pageSize;
    @Schema(description = "오프셋", example = "10")
    private final long offset;
    @Schema(description = "페이징 여부", example = "true")
    private final boolean paged;
    @Schema(description = "정렬 기준")
    private final Sort sort;
    @Schema(description = "응답 코드", example = "200")
    private final int code;
    @Schema(description = "응답 메세지", example = "success")
    private final String message;

    private ApiPageResponse(Page<T> data, int code, String message) {
        this.data = data.getContent();
        this.empty = data.isEmpty();
        this.first = data.isFirst();
        this.last = data.isLast();
        this.totalElements = data.getTotalElements();
        this.pageNumber = data.getNumber();
        this.pageSize = data.getSize();
        this.offset = data.getPageable().getOffset();
        this.paged = data.getPageable().isPaged();
        this.sort = data.getSort();
        this.code = code;
        this.message = message;
    }

    public static <T> ApiPageResponse<T> ok(Page<T> data) {
        return new ApiPageResponse<>(data, 200, "OK");
    }

}