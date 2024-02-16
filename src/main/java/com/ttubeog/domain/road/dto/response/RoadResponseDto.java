package com.ttubeog.domain.road.dto.response;

import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.road.domain.RoadType;
import com.ttubeog.domain.roadcoordinate.domain.RoadCoordinate;
import com.ttubeog.domain.spot.domain.Spot;
import com.ttubeog.domain.store.domain.Store;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
@Builder
public class RoadResponseDto {

    private Long id;

    private RoadType roadType;

    private Long spotId;

    private Long storeId;

    private Long memberId;

    private String name;

    private List<List<Double>> roadCoordinateDoubleList;

}
