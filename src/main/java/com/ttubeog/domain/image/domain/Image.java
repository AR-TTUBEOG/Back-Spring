package com.ttubeog.domain.image.domain;

import com.ttubeog.domain.common.BaseEntity;
import com.ttubeog.domain.spot.domain.Spot;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Many;


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

    @ManyToOne
    @JoinColumn(name = "spot")
    private Spot spot;

    public Image(Long id, String image, Spot spot) {
        this.id = id;
        this.image = image;
        this.spot = spot;
    }
}
