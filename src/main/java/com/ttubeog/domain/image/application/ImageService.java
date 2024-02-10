package com.ttubeog.domain.image.application;

import com.ttubeog.domain.guestbook.domain.repository.GuestBookRepository;
import com.ttubeog.domain.guestbook.exception.InvalidGuestBookIdException;
import com.ttubeog.domain.image.domain.Image;
import com.ttubeog.domain.image.domain.ImageType;
import com.ttubeog.domain.image.domain.repository.ImageRepository;
import com.ttubeog.domain.image.dto.request.CreateImageRequestDto;
import com.ttubeog.domain.image.dto.request.UpdateImageRequestDto;
import com.ttubeog.domain.image.dto.response.ImageResponseDto;
import com.ttubeog.domain.image.exception.InvalidImageException;
import com.ttubeog.domain.image.exception.InvalidImageTypeException;
import com.ttubeog.domain.spot.domain.repository.SpotRepository;
import com.ttubeog.domain.spot.exception.InvalidSpotIdException;
import com.ttubeog.domain.store.domain.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ImageService {

    private final ImageRepository imageRepository;
    private final SpotRepository spotRepository;
    private final StoreRepository storeRepository;
    private final GuestBookRepository guestBookRepository;

    public static List<String> getImageString(List<Image> imageList) {
        List<String> imageString = new ArrayList<>();

        for (Image image : imageList) {
            imageString.add(imageString.size(), image.getImage());
        }

        return imageString;
    }

    public ImageResponseDto findById(Long imageId) {
        Image image = imageRepository.findById(imageId).orElseThrow(InvalidImageException::new);
        ImageResponseDto imageResponseDto = new ImageResponseDto();
        if (image.getImageType().equals(ImageType.SPOT)) {
            imageResponseDto = ImageResponseDto.builder()
                    .id(image.getId())
                    .image(image.getImage())
                    .imageType(image.getImageType())
                    .placeId(image.getSpot().getId())
                    .build();
        } else if (image.getImageType().equals(ImageType.STORE)) {
            imageResponseDto = ImageResponseDto.builder()
                    .id(image.getId())
                    .image(image.getImage())
                    .imageType(image.getImageType())
                    .placeId(image.getStore().getId())
                    .build();
        } else if (image.getImageType().equals(ImageType.GUESTBOOK)) {
            imageResponseDto = ImageResponseDto.builder()
                    .id(image.getId())
                    .image(image.getImage())
                    .imageType(image.getImageType())
                    .placeId(image.getGuestBook().getId())
                    .build();
        } else {
            throw new InvalidImageTypeException();
        }

        return imageResponseDto;
    }

    @Transactional
    public ImageResponseDto createImage(CreateImageRequestDto createImageRequestDto) {

        Image image;
        ImageResponseDto imageResponseDto;
        if (createImageRequestDto.getImageType().equals(ImageType.SPOT)) {
            image = Image.builder()
                    .image(createImageRequestDto.getImage())
                    .imageType(createImageRequestDto.getImageType())
                    .spot(spotRepository.findById(createImageRequestDto.getPlaceId()).orElseThrow(InvalidSpotIdException::new))
                    .build();

            imageResponseDto = ImageResponseDto.builder()
                    .id(image.getId())
                    .imageType(image.getImageType())
                    .placeId(image.getSpot().getId())
                    .build();
        } else if (createImageRequestDto.getImageType().equals(ImageType.STORE)){
            image = Image.builder()
                    .image(createImageRequestDto.getImage())
                    .imageType(createImageRequestDto.getImageType())
                    .store(storeRepository.findById(createImageRequestDto.getPlaceId()).orElseThrow(InvalidSpotIdException::new))
                    .build();

            imageResponseDto = ImageResponseDto.builder()
                    .id(image.getId())
                    .imageType(image.getImageType())
                    .placeId(image.getStore().getId())
                    .build();
        } else if (createImageRequestDto.getImageType().equals(ImageType.GUESTBOOK)) {
            image = Image.builder()
                    .image(createImageRequestDto.getImage())
                    .imageType(createImageRequestDto.getImageType())
                    .store(storeRepository.findById(createImageRequestDto.getPlaceId()).orElseThrow(InvalidSpotIdException::new))
                    .build();

            imageResponseDto = ImageResponseDto.builder()
                    .id(image.getId())
                    .imageType(image.getImageType())
                    .placeId(image.getStore().getId())
                    .build();
        } else {
            throw new InvalidImageTypeException();
        }

        imageRepository.save(image);

        return imageResponseDto;
    }

    @Transactional
    public ImageResponseDto updateImage(UpdateImageRequestDto updateImageRequestDto) {

        Image image = imageRepository.findById(updateImageRequestDto.getId()).orElseThrow(InvalidImageException::new);

        if (!image.getImageType().equals(updateImageRequestDto.getImageType())) {
            throw new InvalidImageTypeException();
        }

        ImageResponseDto imageResponseDto;



        if (updateImageRequestDto.getImageType().equals(ImageType.SPOT)) {
            image.updateImage(updateImageRequestDto.getImage(), spotRepository.findById(updateImageRequestDto.getPlaceId()).orElseThrow(InvalidSpotIdException::new));

            imageResponseDto = ImageResponseDto.builder()
                    .id(image.getId())
                    .imageType(image.getImageType())
                    .placeId(image.getSpot().getId())
                    .build();
        } else if (updateImageRequestDto.getImageType().equals(ImageType.STORE)){
            // TODO 스토어 exception 수정하기
            image.updateImage(updateImageRequestDto.getImage(), storeRepository.findById(updateImageRequestDto.getPlaceId()).orElseThrow(InvalidSpotIdException::new));

            imageResponseDto = ImageResponseDto.builder()
                    .id(image.getId())
                    .imageType(image.getImageType())
                    .placeId(image.getStore().getId())
                    .build();
        } else if (updateImageRequestDto.getImageType().equals(ImageType.GUESTBOOK)) {
            image.updateImage(updateImageRequestDto.getImage(), guestBookRepository.findById(updateImageRequestDto.getPlaceId()).orElseThrow(InvalidGuestBookIdException::new));

            imageResponseDto = ImageResponseDto.builder()
                    .id(image.getId())
                    .imageType(image.getImageType())
                    .placeId(image.getStore().getId())
                    .build();
        } else {
            throw new InvalidImageTypeException();
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
