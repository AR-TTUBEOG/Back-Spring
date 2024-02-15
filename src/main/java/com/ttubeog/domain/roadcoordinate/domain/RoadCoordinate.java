package com.ttubeog.domain.roadcoordinate.domain;

import com.ttubeog.domain.common.BaseEntity;
import com.ttubeog.domain.road.domain.Road;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Tag(name = "road_coordinate")
@Entity
@Getter
@Builder
@NoArgsConstructor
public class RoadCoordinate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "road_id")
    private Road road;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    public RoadCoordinate(Long id, Road road, Double latitude, Double longitude) {
        this.id = id;
        this.road = road;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
