package com.ttubeog.domain.member.domain;

import com.ttubeog.domain.auth.domain.Platform;
import com.ttubeog.domain.auth.domain.Status;
import com.ttubeog.domain.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Builder
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String oAuthId;

    private String name;

    @Size(max = 45)
    @NotNull
    private String memberNumber;

    @Email
    private String email;

    private String password;

    private String platformId;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform")
    private Platform platform;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    public Member(String email, Platform platform, Status status, String memberNumber) {
        this.email = email;
        this.platform = platform;
        this.platformId = platformId;
        this.status = status;
        this.memberNumber = memberNumber;
    }

    public boolean isRegisteredOAuthMember() {
        return name != null;
    }

    public void updateName(String name) {
        this.name = name;
    }

}
