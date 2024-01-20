package com.ttubeog.domain.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

public enum MemberRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private String value;

    MemberRole(String value) {
        this.value = value;
    }

    public String getRole() {
        return value;
    }
}