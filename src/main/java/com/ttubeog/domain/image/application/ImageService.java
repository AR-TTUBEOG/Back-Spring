package com.ttubeog.domain.image.application;

import com.ttubeog.domain.image.domain.Image;
import com.ttubeog.domain.image.domain.repository.ImageRepository;
import com.ttubeog.domain.image.dto.request.CreateImageRequestDto;
import com.ttubeog.domain.image.dto.request.UpdateImageRequestDto;
import com.ttubeog.domain.image.dto.response.ImageResponseDto;
import com.ttubeog.domain.image.exception.InvalidImageException;
import com.ttubeog.domain.spot.domain.repository.SpotRepository;
import com.ttubeog.domain.spot.exception.InvalidSpotIdException;
import com.ttubeog.domain.store.domain.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
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
    public ImageResponseDto createImage(CreateImageRequestDto createImageRequestDto) {

        Image image;
        ImageResponseDto imageResponseDto;
        if (createImageRequestDto.imageRequestType == SPOT) {
            image = Image.builder()
                    .image(createImageRequestDto.getImage())
                    .spot(spotRepository.findById(createImageRequestDto.getPlaceId()).orElseThrow(InvalidSpotIdException::new))
                    .build();

            imageResponseDto = ImageResponseDto.builder()
                    .id(image.getId())
                    .placeId(image.getSpot().getId())
                    .build();
        } else {
            image = Image.builder()
                    .image(createImageRequestDto.getImage())
                    .store(storeRepository.findById(createImageRequestDto.getPlaceId()).orElseThrow(InvalidSpotIdException::new))
                    .build();

            imageResponseDto = ImageResponseDto.builder()
                    .id(image.getId())
                    .placeId(image.getStore().getId())
                    .build();
        }
        imageRepository.save(image);

        return imageResponseDto;
    }

    @Transactional
    public ImageResponseDto updateImage(UpdateImageRequestDto updateImageRequestDto) {

        Image image = imageRepository.findById(updateImageRequestDto.getId()).orElseThrow(InvalidImageException::new);
        ImageResponseDto imageResponseDto;

        if (updateImageRequestDto.imageRequestType == SPOT) {
            image.updateImage(updateImageRequestDto.getImage(), spotRepository.findById(updateImageRequestDto.getPlaceId()).orElseThrow(InvalidSpotIdException::new));
            imageResponseDto = ImageResponseDto.builder()
                    .id(image.getId())
                    .placeId(image.getSpot().getId())
                    .build();
        } else {
            image.updateImage(updateImageRequestDto.getImage(), storeRepository.findById(updateImageRequestDto.getPlaceId()).orElseThrow(InvalidSpotIdException::new));
            imageResponseDto = ImageResponseDto.builder()
                    .id(image.getId())
                    .placeId(image.getStore().getId())
                    .build();
        }

        imageRepository.save(image);

        return imageResponseDto;
    }

    @Transactional
    public void deleteImage(Long imageId) {

        Image image = imageRepository.findById(imageId).orElseThrow(InvalidImageException::new);
        imageRepository.delete(image);

    }
}
