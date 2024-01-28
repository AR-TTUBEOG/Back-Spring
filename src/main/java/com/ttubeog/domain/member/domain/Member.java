package com.ttubeog.domain.member.domain;

import com.ttubeog.domain.auth.domain.Platform;
import com.ttubeog.domain.auth.domain.Status;
import com.ttubeog.domain.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String oAuthId;

    private String name;

    @Email
    private String email;

    private String imageUrl;

    private String password;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    private String platformId;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform")
    private Platform platform;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    public Member(Long id, String name, String email, String imageUrl, String password, Provider provider, MemberRole memberRole, String platformId, Platform platform, String refreshToken, Status status, int reportCount) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.password = password;
        this.provider = provider;
        this.memberRole = memberRole;
        this.platformId = platformId;
        this.platform = platform;
        this.refreshToken = refreshToken;
        this.status = status;
    }

    public Member(String email, Platform platform, Status status) {
        this.email = email;
        this.platform = platform;
        this.platformId = platformId;
        this.status = status;
    }

    public boolean isRegisteredOAuthMember() {
        return name != null;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
