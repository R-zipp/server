package com.mtvs.arzip.domain.dto.ai_drawing_data;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiDrawingDataResponse {

    private String drawingType;
    private String userDrawingImage;

}
