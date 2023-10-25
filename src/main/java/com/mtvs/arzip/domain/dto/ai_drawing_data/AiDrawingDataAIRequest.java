package com.mtvs.arzip.domain.dto.ai_drawing_data;

import com.mtvs.arzip.domain.entity.AIDrawingData;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiDrawingDataAIRequest {

    private String fbxFile;

    public static AIDrawingData toEntity(AiDrawingDataAIRequest dto) {
        return AIDrawingData.builder()
                .fbxFile(dto.fbxFile)
                .build();
    }


}
