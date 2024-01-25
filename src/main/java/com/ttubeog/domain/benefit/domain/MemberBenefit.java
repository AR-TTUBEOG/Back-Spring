package com.ttubeog.domain.benefit.domain;

import com.ttubeog.domain.common.BaseEntity;
import com.ttubeog.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MemberBenefit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "benefit_id")
    private Benefit benefit;

    private Boolean used = false; // 사용 여부

    private Boolean expired = false; // 만료 여부

    @Builder
    public MemberBenefit(Member member, Benefit benefit, Boolean used, Boolean expired) {
        this.member = member;
        this.benefit = benefit;
        this.used = used;
        this.expired = expired;
    }

    public void useBenefit() {
        this.used = true;
    }

    public void terminateBenefit() {
        this.expired = true;
    }
}
