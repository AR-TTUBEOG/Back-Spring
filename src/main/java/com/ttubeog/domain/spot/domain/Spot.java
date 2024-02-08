package com.ttubeog.domain.spot.domain;

import com.ttubeog.domain.area.domain.DongArea;
import com.ttubeog.domain.common.BaseEntity;
import com.ttubeog.domain.image.domain.Image;
import com.ttubeog.domain.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "산책 스팟 이름", example = "서울 성곽 길")
    @Column(name = "name", nullable = false)
    private String name;

    @Schema(description = "세부 정보", example = "서울 성곽 길은 조선 시대에 건축된 한성 성곽을 따라 조성된 산책로입니다.")
    @Column(name = "info", nullable = false)
    private String info;

    @Schema(description = "위도", example = "36.5642886")
    @Column(name = "latitude", nullable = false)
    private Float latitude;

    @Schema(description = "경도", example = "126.9275718")
    @Column(name = "longitude", nullable = false)
    private Float longitude;

    @Schema(description = "별점 평균", example = "3.4")
    @Column(name = "stars")
    private Float stars;

    @Schema(description = "등록 유저", example = "Member Entity")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Schema(description = "지역 코드", example = "DongArea Entity")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dong_area_id", nullable = false)
    private DongArea dongArea;

    @Schema(description = "상세 주소", example = "301호")
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
