package com.mtvs.arzip.domain.dto.ai_drawing_data;

import com.mtvs.arzip.domain.entity.AIDrawingData;
import com.mtvs.arzip.domain.enum_class.DrawingType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiDrawingDataAIRequest {

    private String drawingType;
    private String fbxFile;
    private String houseSize;
    private int wallPaperNo;

    public static AIDrawingData toEntity(AiDrawingDataAIRequest dto) {
        return AIDrawingData.builder()
                .drawingType(DrawingType.valueOf(dto.drawingType))
                .fbxFile(dto.fbxFile)
                .houseSize(dto.houseSize)
                .wallPaperNo(dto.wallPaperNo)
                .build();
    }


}
