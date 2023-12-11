package com.mtvs.arzip.domain.dto.ar_space_data;

import com.mtvs.arzip.domain.entity.ARObjectPlacementData;
import com.mtvs.arzip.domain.entity.ARSpaceData;
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
    private String title;
    private Integer views;
//    private Integer maxConnectionCount;
    private String nickname;
    private String fbxFile;
    private Long aiDrawingDataNo;
    private List<ARObjectPlacementDataResponse> placements;
    private int wallPaperNo;

    public ARSpaceDataResponse(ARSpaceData arSpaceData, List<ARObjectPlacementData> placements) {
        this.spaceNo = arSpaceData.getNo();
        this.title = arSpaceData.getTitle();
        this.views = arSpaceData.getViews();
//        this.maxConnectionCount = arSpaceData.getMaxConnectionCount();
        this.nickname = arSpaceData.getUser().getNickname();
        this.fbxFile = arSpaceData.getAiDrawingData().getFbxFile();
        this.aiDrawingDataNo = arSpaceData.getAiDrawingData().getNo();
        this.placements = placements.stream().map(ARObjectPlacementDataResponse::new).collect(Collectors.toList());
        this.wallPaperNo = arSpaceData.getAiDrawingData().getWallPaperNo();
    }

}
