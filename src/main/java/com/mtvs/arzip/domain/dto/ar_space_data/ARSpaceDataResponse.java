package com.mtvs.arzip.domain.dto.ar_space_data;

import com.mtvs.arzip.domain.entity.AIDrawingData;
import com.mtvs.arzip.domain.entity.ARObjectPlacementData;
import com.mtvs.arzip.domain.entity.ARSpaceData;
import com.mtvs.arzip.domain.entity.User;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ARSpaceDataResponse {

    private Long spaceNo;
    private Integer views;
    private Integer maxConnectionCount;
    private User user;
    private AIDrawingData aiDrawingData;
    private List<ARObjectPlacementDataResponse> placements;

    public ARSpaceDataResponse(ARSpaceData arSpaceData, List<ARObjectPlacementData> placements) {
        this.spaceNo = arSpaceData.getNo();
        this.views = arSpaceData.getViews();
        this.maxConnectionCount = arSpaceData.getMaxConnectionCount();
        this.user = arSpaceData.getUser();
        this.aiDrawingData = arSpaceData.getAiDrawingData();
        this.placements = placements.stream().map(ARObjectPlacementDataResponse::new).collect(Collectors.toList());
    }

}
