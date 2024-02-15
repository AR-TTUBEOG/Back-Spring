package com.ttubeog.domain.road.domain;

import com.ttubeog.domain.common.BaseEntity;
import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.spot.domain.Spot;
import com.ttubeog.domain.store.domain.Store;
import jakarta.persistence.*;

@Entity
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

    @Column(name = "road_file", nullable = false)
    private String roadFile;

    @Column(name = "time", nullable = false)
    private String time;
}
