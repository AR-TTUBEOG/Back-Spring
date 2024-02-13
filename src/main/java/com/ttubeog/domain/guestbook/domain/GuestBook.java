package com.ttubeog.domain.guestbook.domain;

import com.ttubeog.domain.common.BaseEntity;
import com.ttubeog.domain.store.domain.Store;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class GuestBook extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;
}
