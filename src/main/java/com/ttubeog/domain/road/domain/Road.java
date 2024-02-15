package com.ttubeog.domain.road.domain;

import com.ttubeog.domain.common.BaseEntity;
import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.roadcoordinate.domain.RoadCoordinate;
import com.ttubeog.domain.spot.domain.Spot;
import com.ttubeog.domain.store.domain.Store;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
public class Road extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id")
    private Spot spot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "road", cascade = CascadeType.ALL)
    private List<RoadCoordinate> roadCoordinateList;

    @Column(name = "time", nullable = false)
    private String time;

    public Road(Long id, Spot spot, Store store, Member member, String name, List<RoadCoordinate> roadCoordinateList, String time) {
        this.id = id;
        this.spot = spot;
        this.store = store;
        this.member = member;
        this.name = name;
        this.roadCoordinateList = roadCoordinateList;
        this.time = time;
    }
}
