package com.ttubeog.domain.place.domain;

import lombok.Builder;
import lombok.Data;

@Data
public class PlaceType {

    private boolean store;
    private boolean spot;

    @Builder
    public PlaceType(boolean store, boolean spot) {
        this.store = store;
        this.spot = spot;
    }
}
