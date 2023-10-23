package com.mtvs.arzip.domain.dto.ai_drawing_data;

import com.mtvs.arzip.domain.entity.AIDrawingData;
import com.mtvs.arzip.domain.enum_class.DrawingType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiDrawingDataUnrealDto {

    private String drawingType;
    private String userDrawingImage;

    public static AIDrawingData toEntity(AiDrawingDataUnrealDto dto) {
        return AIDrawingData.builder()
                .drawingType(DrawingType.valueOf(dto.getDrawingType()))
                .userDrawingImage(dto.getUserDrawingImage())
                .build();
    }
}
