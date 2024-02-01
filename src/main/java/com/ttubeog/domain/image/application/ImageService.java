package com.ttubeog.domain.image.application;

import com.ttubeog.domain.image.domain.Image;
import com.ttubeog.domain.image.domain.repository.ImageRepository;
import com.ttubeog.domain.image.dto.request.ImageRequestDto;
import com.ttubeog.domain.image.dto.response.ImageResponseDto;
import com.ttubeog.domain.spot.domain.Spot;
import com.ttubeog.domain.spot.domain.repository.SpotRepository;
import com.ttubeog.domain.spot.exception.InvalidSpotIdException;
import com.ttubeog.domain.store.domain.repository.StoreRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.ttubeog.domain.image.dto.request.ImageRequestType.SPOT;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ImageService {

    private final ImageRepository imageRepository;
    private final SpotRepository spotRepository;
    private final StoreRepository storeRepository;

    public static List<String> getImageString(List<Image> imageList) {
        List<String> imageString = new ArrayList<>();

        for (Image image : imageList) {
            imageString.add(imageString.size(), image.getImage());
        }

        return imageString;
    }

    @Transactional
    public ImageResponseDto createImage(ImageRequestDto imageRequestDto) {
        Image image;
        ImageResponseDto imageResponseDto;
        if (imageRequestDto.imageRequestType == SPOT) {
            image = Image.builder()
                    .image(imageRequestDto.getImage())
                    .spot(spotRepository.findById(imageRequestDto.getPlaceId()).orElseThrow(InvalidSpotIdException::new))
                    .build();

            imageResponseDto = ImageResponseDto.builder()
                    .id(image.getId())
                    .placeId(image.getSpot().getId())
                    .build();
        } else {
            image = Image.builder()
                    .image(imageRequestDto.getImage())
                    .store(storeRepository.findById(imageRequestDto.getPlaceId()).orElseThrow(InvalidSpotIdException::new))
                    .build();

            imageResponseDto = ImageResponseDto.builder()
                    .id(image.getId())
                    .placeId(image.getStore().getId())
                    .build();
        }
        imageRepository.save(image);

        return imageResponseDto;
    }

}
