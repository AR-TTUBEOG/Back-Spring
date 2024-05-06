package com.ttubeog.domain.area.domain;

import com.ttubeog.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "dong_area")
public class DongArea extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dong_name")
    private String dongName;
}
