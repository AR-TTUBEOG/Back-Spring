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

    private String nickname;

    @Size(max = 45)
    @NotNull
    private String memberNumber;

    @Email
    private String email;

    private String platformId;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform")
    private Platform platform;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "nickname_changed")
    private Boolean nicknameChange;


    public Member(String email, Platform platform, Status status, String memberNumber, Boolean nicknameChange) {
        this.email = email;
        this.platform = platform;
        this.platformId = platformId;
        this.status = status;
        this.memberNumber = memberNumber;
        this.nicknameChange = nicknameChange;
    }

    public Member(Status status) {
        this.status = status;
    }
    public boolean isRegisteredOAuthMember() {
        return nickname != null;
    }

    public void updateName(String name) {
        this.nickname = name;
    }

}
