package com.ttubeog.domain.UuidImage.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Builder
@Table(name = "uuid_image")
public class UuidImage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String uuid;

    @Column(name = "image_url")
    private String imageUrl;

    public UuidImage(Long id, String uuid, String imageUrl) {
        this.id = id;
        this.uuid = uuid;
        this.imageUrl = imageUrl;
    }

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
