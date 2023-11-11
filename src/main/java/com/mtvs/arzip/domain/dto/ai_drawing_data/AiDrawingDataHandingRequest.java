package com.mtvs.arzip.domain.dto.ai_drawing_data;

import com.mtvs.arzip.domain.entity.AIDrawingData;
import com.mtvs.arzip.domain.enum_class.DrawingType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiDrawingDataHandingRequest {

    private String drawingType;
    private String userDrawingImage;
    private String houseSize;

    public static AIDrawingData toEntity(AiDrawingDataHandingRequest dto) {
        return AIDrawingData.builder()
                .drawingType(DrawingType.HANDIMG)
                .userDrawingImage(dto.getUserDrawingImage())
                .houseSize(dto.houseSize)
                .build();
    }
}
