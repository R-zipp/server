package com.mtvs.arzip.domain.dto.ar_space_data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mtvs.arzip.domain.entity.AIDrawingData;
import com.mtvs.arzip.domain.entity.ARSpaceData;
import com.mtvs.arzip.domain.entity.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ARSpaceDataRequest {

    private Integer views;
//    private Integer maxConnectionCount;
    private String title;
    private Long userNo;
    private Long aiDrawingDataNo;
    private List<ARObjectPlacementDataRequest> placements = new ArrayList<>();

    @JsonCreator
    public ARSpaceDataRequest(@JsonProperty("views") Integer views,
                              @JsonProperty("title") String title,
                              @JsonProperty("placements") List<ARObjectPlacementDataRequest> placements) {
        this.views = views;
        this.title = title;
        this.placements = placements;
    }

    public ARSpaceData toEntity(User user, AIDrawingData aiDrawingData) {
        return ARSpaceData.builder()
                .views(views)
//                .maxConnectionCount(maxConnectionCount)
                .title(this.title)
                .user(user)
                .aiDrawingData(aiDrawingData)
                .build();
    }

}
