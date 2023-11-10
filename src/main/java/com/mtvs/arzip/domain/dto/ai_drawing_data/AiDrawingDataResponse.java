package com.mtvs.arzip.domain.dto.ai_drawing_data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiDrawingDataResponse {

    // unreal 온 데이터
    private String drawingType;
    private String userDrawingImage;
    private Integer houseSize;

    // ai에서 넣어야 할 url 경로
    @JsonProperty("URL") // JSON 키를 "URL"로 명시적으로 매핑
    private String fbxFile;


}
