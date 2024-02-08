package com.ttubeog.domain.guestbook.domain;

import com.ttubeog.domain.common.BaseEntity;
import com.ttubeog.domain.image.domain.Image;
import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.spot.domain.Spot;
import com.ttubeog.domain.store.domain.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@Entity
@Table(name = "guest_book")
public class GuestBook extends BaseEntity {

    @Schema(description = "방명록 ID", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "guest_book_type", nullable = false)
    private GuestBookType guestBookType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id")
    private Spot spot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    private String content;

    private Float star;

    @OneToOne(mappedBy = "guestBook", cascade = CascadeType.ALL)
    private Image image;

    public GuestBook(Long id, Member member, GuestBookType guestBookType, Spot spot, Store store, String content, Float star, Image image) {
        this.id = id;
        this.member = member;
        this.guestBookType = guestBookType;
        this.spot = spot;
        this.store = store;
        this.content = content;
        this.star = star;
        this.image = image;
    }

    public void updateGuestBook(String content, Float star, Image image) {
        this.content = content;
        this.star = star;
        this.image = image;
    }

}
