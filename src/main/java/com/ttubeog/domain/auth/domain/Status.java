package com.ttubeog.domain.auth.domain;

import com.ttubeog.domain.auth.exception.InvalidStatusException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum Status {
    ACTIVE("active"),
    INACTIVE("inactive");

    private String value;

    public static Status from(String value) {
        return Arrays.stream(values())
                .filter(it -> Objects.equals(it.value, value))
                .findFirst()
                .orElseThrow(InvalidStatusException::new);
    }
}
