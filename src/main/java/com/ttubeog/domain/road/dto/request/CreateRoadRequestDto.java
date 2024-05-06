package com.ttubeog.domain.road.dto.request;

import com.ttubeog.domain.road.domain.RoadType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
public class CreateRoadRequestDto {

    @Schema(description = "길 종류", defaultValue = "SPOT")
    private RoadType roadType;

    @Schema(description = "산책스팟 ID", defaultValue = "1")
    private Long spotId;

    @Schema(description = "매장 ID", defaultValue = "1")
    private Long storeId;

    @Schema(description = "길 이름", defaultValue = "스티브의 길")
    private String name;

    @Schema(description = "좌표 포인트 배열", defaultValue = "[\n" +
            "[37.50286865234375, 126.95667670044992],\n" +
            "[37.5029296875, 126.95677684301509],\n" +
            "[37.5030517578125, 126.9568895358081],\n" +
            "[37.503204345703125, 126.95712283976323],\n" +
            "[37.503173828125, 126.95706043328734],\n" +
            "[37.503143310546875, 126.95698617501382],\n" +
            "[37.503204345703125, 126.95701685426971]\n" +
            "]")
    private List<List<Double>> roadCoordinateList;

}