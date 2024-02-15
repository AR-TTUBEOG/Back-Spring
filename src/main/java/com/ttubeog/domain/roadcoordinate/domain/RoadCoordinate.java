package com.ttubeog.domain.roadcoordinate.domain;

import com.ttubeog.domain.common.BaseEntity;
import com.ttubeog.domain.road.domain.Road;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Builder
@Table(name = "road_coordinate")
public class RoadCoordinate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "road_id")
    private Road road;

    @Column(name = "latitude", nullable = false)
    private Float latitude;

    @Column(name = "longitude", nullable = false)
    private Float longitude;

    public RoadCoordinate(Long id, Road road, Float latitude, Float longitude) {
        this.id = id;
        this.road = road;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void updateRoad(Road road) {
        this.road = road;
    }
}
