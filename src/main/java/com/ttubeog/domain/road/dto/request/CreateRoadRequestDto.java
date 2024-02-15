package com.ttubeog.domain.road.dto.request;

import com.ttubeog.domain.road.domain.RoadType;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
public class CreateRoadRequestDto {

    private RoadType roadType;

    private Long spotId;

    private Long storeId;

    private Long memberId;

    private String name;

    private List<List<Double>> roadCoordinateList;

    private String time;

}
