package com.mtvs.arzip.domain.dto.ar_space_data;

import com.mtvs.arzip.domain.entity.AIDrawingData;
import com.mtvs.arzip.domain.entity.ARSpaceData;
import com.mtvs.arzip.domain.entity.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ARSpaceDataRequest {

    private Integer views;
    private Integer maxConnectionCount;
    private Long userNo;
    private Long aiDrawingDataNo;
    private List<ARObjectPlacementDataRequest> placements;

    public ARSpaceData toEntity(User user, AIDrawingData aiDrawingData) {
        return ARSpaceData.builder()
                .views(views)
                .maxConnectionCount(maxConnectionCount)
                .user(user)
                .aiDrawingData(aiDrawingData)
                .build();
    }

}