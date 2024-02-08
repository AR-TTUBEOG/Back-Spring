package com.ttubeog.domain.store.domain;

import com.ttubeog.domain.area.domain.DongArea;
import com.ttubeog.domain.common.BaseEntity;
import com.ttubeog.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "store")
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "info")
    private String info;

    @Column(name = "detailAddress")
    private String detailAddress;

    @Column(name = "latitude")
    private Float latitude;

    @Column(name = "longitude")
    private Float longitude;

    @Column(name = "image")
    private String image;

    @Column(name = "stars")
    private Float stars;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private StoreType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dong_area_id")
    private DongArea dongArea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void updateName(String name) {
        this.name = name;
    }

    public void updateInfo(String info) {
        this.info = info;
    }

    public void updateDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public void updateLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public void updateLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public void updateImage(String image) {
        this.image = image;
    }

    public void updateType(StoreType type) {
        this.type = type;
    }
}
