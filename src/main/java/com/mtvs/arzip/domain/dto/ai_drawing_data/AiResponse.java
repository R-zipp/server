package com.mtvs.arzip.domain.dto.ai_drawing_data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AiResponse {

    @JsonProperty("URL")
    private String URL;

}
