package com.ttubeog.domain.spot.domain;

import com.ttubeog.domain.area.domain.DongArea;
import com.ttubeog.domain.common.BaseEntity;
import com.ttubeog.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Many;
import org.hibernate.annotations.Fetch;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "spot")
public class Spot extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "info")
    private String info;

    @Column(name = "latitude")
    private Float latitude;

    @Column(name = "longitude")
    private Float longitude;

    @Column(name = "image")
    private String image;

    @Column(name = "starts")
    private Float stars;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id)")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dong_area_id")
    private DongArea dongArea;

    @Column(name = "detail_address")
    private String detailAddress;

    public Spot(Long id, String name, String info, Float latitude, Float longitude, String image, Float stars, Member member, DongArea dongArea, String detailAddress) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.stars = stars;
        this.member = member;
        this.dongArea = dongArea;
        this.detailAddress = detailAddress;
    }
}
