package com.ttubeog.domain.spot.domain;

import com.ttubeog.domain.area.domain.DongArea;
import com.ttubeog.domain.common.BaseEntity;
import com.ttubeog.domain.image.domain.Image;
import com.ttubeog.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Many;
import org.hibernate.annotations.Fetch;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@Entity
@Table(name = "spot")
public class Spot extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "info", nullable = false)
    private String info;

    @Column(name = "latitude", nullable = false)
    private Float latitude;

    @Column(name = "longitude", nullable = false)
    private Float longitude;

    @Column(name = "starts")
    private Float stars;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dong_area_id", nullable = false)
    private DongArea dongArea;

    @Column(name = "detail_address", nullable = false)
    private String detailAddress;

    public Spot(Long id, String name, String info, Float latitude, Float longitude, Float stars, Member member, DongArea dongArea, String detailAddress) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.latitude = latitude;
        this.longitude = longitude;
        this.stars = stars;
        this.member = member;
        this.dongArea = dongArea;
        this.detailAddress = detailAddress;
    }

    public void updateSpot(String name, String info, Float latitude, Float longitude, DongArea dongArea, String detailAddress) {
        this.name = name;
        this.info = info;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dongArea = dongArea;
        this.detailAddress = detailAddress;
    }

    public void updateStars(Float stars) {
        this.stars = stars;
    }

}
