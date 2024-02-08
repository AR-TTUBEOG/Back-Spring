package com.ttubeog.domain.member.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "ProduceNicknameRequest")
public class ProduceNicknameRequest {

    @Schema(description = "닉네임", example = "푸")
    private String nickname;
}
