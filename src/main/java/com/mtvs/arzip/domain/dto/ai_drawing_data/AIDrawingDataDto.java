package com.mtvs.arzip.domain.dto.ai_drawing_data;

import com.mtvs.arzip.domain.entity.AIDrawingData;
import com.mtvs.arzip.domain.enum_class.DrawingType;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AIDrawingDataDto {

    private Long no;
    @Enumerated(EnumType.STRING)
    private DrawingType drawingType;
    private String userDrawingImage;
    private String fbxFile;
    private String houseSize;
    private int wallPaperNo;

    public static AIDrawingDataDto of (AIDrawingData aiDrawingData) {
        return AIDrawingDataDto.builder()
                .no(aiDrawingData.getNo())
                .drawingType(aiDrawingData.getDrawingType())
                .userDrawingImage(aiDrawingData.getUserDrawingImage())
                .fbxFile(aiDrawingData.getFbxFile())
                .houseSize(aiDrawingData.getHouseSize())
                .wallPaperNo(aiDrawingData.getWallPaperNo())
                .build();
    }
}
