package com.mtvs.arzip.domain.dto.ai_drawing_data;

import com.mtvs.arzip.domain.entity.AIDrawingData;
import com.mtvs.arzip.domain.enum_class.DrawingType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiDrawingDataFloorPlanRequest {

    private String drawingType;
    private String userDrawingImage;
    private String houseSize;
    private int wallPaperNo;

    public static AIDrawingData toEntity(AiDrawingDataFloorPlanRequest dto) {
        return AIDrawingData.builder()
                .drawingType(DrawingType.FLOORPLAN)
                .userDrawingImage(dto.getUserDrawingImage())
                .houseSize(dto.houseSize)
                .wallPaperNo(dto.wallPaperNo)
                .build();
    }
}
