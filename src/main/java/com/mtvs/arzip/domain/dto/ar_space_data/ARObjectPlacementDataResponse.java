package com.mtvs.arzip.domain.dto.ar_space_data;

import com.mtvs.arzip.domain.entity.ARObjectPlacementData;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ARObjectPlacementDataResponse {

    private Long no;
    private double x;
    private double y;
    private double z;
    private double rotation;
    private Long arSpaceDataNo;
    private Long objectInfoNo;

    public ARObjectPlacementDataResponse(ARObjectPlacementData placementData) {
        this.no = placementData.getNo();
        this.x = placementData.getX();
        this.y = placementData.getY();
        this.z = placementData.getZ();
        this.rotation = placementData.getRotation();
        this.arSpaceDataNo = placementData.getArSpaceData().getNo();
        this.objectInfoNo = placementData.getObjectInfo().getNo();
    }
}