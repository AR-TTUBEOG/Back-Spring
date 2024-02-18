package com.ttubeog.domain.image.presentation;

import com.ttubeog.domain.image.application.ImageService;
import com.ttubeog.global.config.security.token.CurrentUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Image", description = "Image API(이미지 API)")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/image")
public class ImageController {

    private final ImageService imageService;

    @PostMapping(value = "/spot", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<?> createSpotImage(
            @CurrentUser HttpServletRequest request,
            @RequestParam Long spotId,
            @RequestPart List<MultipartFile> fileList
    ) {
        return imageService.createSpotImage(request, spotId, fileList);
    }

    @PostMapping(value = "/store", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<?> createStoreImage(
            @CurrentUser HttpServletRequest request,
            @RequestParam Long storeId,
            @RequestPart List<MultipartFile> fileList
    ) {
        return imageService.createStoreImage(request, storeId, fileList);
    }

    @PostMapping(value = "/guestbook", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<?> createGuestBookImage(
            @CurrentUser HttpServletRequest request,
            @RequestParam Long guestBookId,
            @RequestPart List<MultipartFile> fileList
    ) {
        return imageService.createGuestBookImage(request, guestBookId, fileList);
    }

    @GetMapping(value = "/{spotId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> findImageBySpotId(
            @CurrentUser HttpServletRequest request,
            @RequestParam Long spotId
    ) {
        return imageService.findImageBySpotId(request, spotId);
    }

    @GetMapping(value = "/{storeId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> findImageByStoreId(
            @CurrentUser HttpServletRequest request,
            @RequestParam Long storeId
    ) {
        return imageService.findImageByStoreId(request, storeId);
    }

    @GetMapping(value = "/{guestBookId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> findImageByGuestBookId(
            @CurrentUser HttpServletRequest request,
            @RequestParam Long guestBookId
    ) {
        return imageService.findImageByGuestBookId(request, guestBookId);
    }


    @DeleteMapping(value = "/{spotId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> deleteImageBySpotId(
            @RequestParam Long spotId
    ) {
        return imageService.deleteImageBySpotId(spotId);
    }

    @DeleteMapping(value = "/{storeId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> deleteImageByStoreId(
            @RequestParam Long storeId
    ) {
        return imageService.deleteImageByStoreId(storeId);
    }

    @DeleteMapping(value = "/{guestBookId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> deleteImageByGuestBookId(
            @RequestParam Long guestBookId
    ) {
        return imageService.deleteImageByGuestBookId(guestBookId);
    }
}
