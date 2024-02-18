package com.ttubeog.domain.image.dto.response;

import com.ttubeog.domain.image.domain.ImageType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponseDto {

    @Schema(description = "이미지 ID", defaultValue = "1")
    public Long id;

    @Schema(description = "랜덤 고유 식별자", defaultValue = "100")
    public String uuid;

    @Schema(description = "이미지", defaultValue = "url")
    public String image;

    @Schema(description = "이미지 타입", defaultValue = "SPOT")
    public ImageType imageType;

    @Schema(description = "장소 ID", defaultValue = "1")
    public Long placeId;

}