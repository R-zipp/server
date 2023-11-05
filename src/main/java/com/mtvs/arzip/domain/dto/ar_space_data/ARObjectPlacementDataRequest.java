package com.mtvs.arzip.domain.dto.ar_space_data;

import com.mtvs.arzip.domain.entity.ARObjectPlacementData;
import com.mtvs.arzip.domain.entity.ARSpaceData;
import com.mtvs.arzip.domain.entity.ObjectInfo;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ARObjectPlacementDataRequest {

    private Integer x;
    private Integer y;
    private Integer z;
    private Integer rotation;
    private Long objectInfoNo;

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
