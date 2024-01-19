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

    private Boolean is_used = false; // 사용 여부

    private Boolean has_expired = false; // 만료 여부

    @Builder
    public MemberBenefit(Long id, Member member, Benefit benefit, Boolean is_used, Boolean has_expired) {
        this.id = id;
        this.member = member;
        this.benefit = benefit;
        this.is_used = is_used;
        this.has_expired = has_expired;
    }
}
