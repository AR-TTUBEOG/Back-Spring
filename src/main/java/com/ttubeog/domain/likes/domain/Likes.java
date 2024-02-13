package com.ttubeog.domain.likes.domain;

import com.ttubeog.domain.common.BaseEntity;
import com.ttubeog.domain.store.domain.Store;
import jakarta.persistence.*;

@Entity
public class Likes extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;
}
