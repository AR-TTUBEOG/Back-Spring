package com.ttubeog.domain.image.domain;

import com.ttubeog.domain.common.BaseEntity;
import com.ttubeog.domain.guestbook.domain.GuestBook;
import com.ttubeog.domain.spot.domain.Spot;
import com.ttubeog.domain.store.domain.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Many;

import javax.imageio.ImageTypeSpecifier;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@Entity
@Table(name = "image")
public class Image extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "image_type", nullable = false)
    private ImageType imageType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot")
    private Spot spot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store")
    private Store store;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guestbook_id")
    private GuestBook guestBook;

    public Image(Long id, String image, ImageType imageType, Spot spot, Store store, GuestBook guestBook) {
        this.id = id;
        this.image = image;
        this.imageType = imageType;
        this.spot = spot;
        this.store = store;
        this.guestBook = guestBook;
    }

    public void updateImage(String image, Spot spot) {
        this.image = image;
        this.spot = spot;
    }

    public void updateImage(String image, Store store) {
        this.image = image;
        this.store = store;
    }

    public void updateImage(String image, GuestBook guestBook) {
        this.image = image;
        this.guestBook = guestBook;
    }
}
