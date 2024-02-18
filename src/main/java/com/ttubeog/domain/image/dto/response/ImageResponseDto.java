package com.ttubeog.domain.image.dto.response;

import com.ttubeog.domain.image.domain.ImageType;
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

    public Long id;
    public String uuid;
    public String image;
    public ImageType imageType;
    public Long placeId;

}