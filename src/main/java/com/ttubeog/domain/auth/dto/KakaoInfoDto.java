package com.ttubeog.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoInfoDto {
    private long id;

    @JsonCreator
    public KakaoInfoDto(@JsonProperty("id") long id) {
        this.id = id;
    }
}
