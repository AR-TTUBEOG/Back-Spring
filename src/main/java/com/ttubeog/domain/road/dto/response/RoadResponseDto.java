package com.ttubeog.domain.road.dto.response;

import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.road.domain.RoadType;
import com.ttubeog.domain.roadcoordinate.domain.RoadCoordinate;
import com.ttubeog.domain.spot.domain.Spot;
import com.ttubeog.domain.store.domain.Store;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoadResponseDto {

    private Long id;

    private RoadType roadType;

    private Spot spot;

    private Store store;

    private Member member;

    private String name;

    private List<RoadCoordinate> roadCoordinate;

    private String time;

}
