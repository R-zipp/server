package com.mtvs.arzip.domain.dto.ar_space_data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mtvs.arzip.domain.entity.ARObjectPlacementData;
import com.mtvs.arzip.domain.entity.ARSpaceData;
import com.mtvs.arzip.domain.entity.ObjectInfo;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class ARObjectPlacementDataRequest {

    private double x;
    private double y;
    private double z;
    private double rotation;
    private Long objectInfoNo;

    @JsonCreator
    public ARObjectPlacementDataRequest(@JsonProperty("x") double x,
                                        @JsonProperty("y") double y,
                                        @JsonProperty("z") double z,
                                        @JsonProperty("rotation") double rotation,
                                        @JsonProperty("objectInfoNo") Long objectInfoNo) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotation = rotation;
        this.objectInfoNo = objectInfoNo;
    }


    public ARObjectPlacementData toEntity(ARSpaceData arSpaceData, ObjectInfo objectInfo) {
        return ARObjectPlacementData.builder()
                .x(x)
                .y(y)
                .z(z)
                .rotation(rotation)
                .arSpaceData(arSpaceData)
                .objectInfo(objectInfo)
                .build();
    }

}
