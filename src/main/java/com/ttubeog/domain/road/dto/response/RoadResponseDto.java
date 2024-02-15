package com.ttubeog.domain.road.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
@Builder
public class RoadResponseDto {

    private Long id;

    private Long spotId;

    private Long storeId;

    private Long memberId;

    private String name;

    private List<List<Double>> roadCoordinateList;

    private String time;

}
