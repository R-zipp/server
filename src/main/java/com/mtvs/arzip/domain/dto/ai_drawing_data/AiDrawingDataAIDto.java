package com.mtvs.arzip.domain.dto.ai_drawing_data;

import com.mtvs.arzip.domain.entity.AIDrawingData;
import com.mtvs.arzip.domain.enum_class.DrawingType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiDrawingDataAIDto {

    private String aiDrawingImage;

    public static AIDrawingData toEntity(AiDrawingDataAIDto dto) {
        return AIDrawingData.builder()
                .aiDrawingImage(dto.aiDrawingImage)
                .build();
    }


}
