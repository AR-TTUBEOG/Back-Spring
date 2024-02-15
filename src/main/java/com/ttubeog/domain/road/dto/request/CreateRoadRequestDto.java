package com.ttubeog.domain.road.dto.request;

import com.ttubeog.domain.road.domain.RoadType;
import lombok.Data;

import java.util.List;

@Data
public class CreateRoadRequestDto {

    private RoadType roadType;

    private Long spotId;

    private Long storeId;

    private String name;

    private List<List<Float>> roadCoordinate;

    private String time;

}
