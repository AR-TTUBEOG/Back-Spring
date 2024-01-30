package com.ttubeog.domain.image.domain;

import com.ttubeog.domain.common.BaseEntity;
import com.ttubeog.domain.spot.domain.Spot;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@Entity
@Table(name = "image")
public class Image extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id")
    private Spot spot;

    @Column(name = "image")
    private String image;

    public Image(Long id, Spot spot, String image) {
        this.id = id;
        this.spot = spot;
        this.image = image;
    }
}
