package com.mtvs.arzip.domain.dto.ar_space_data;

import com.mtvs.arzip.domain.entity.ARSpaceData;
import lombok.*;
import org.springframework.data.domain.Page;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ARSpaceListResponse {

    private String title;
    private String nickname;

    public static Page<ARSpaceListResponse> listResponses (Page<ARSpaceData> arSpaceData) {
        Page<ARSpaceListResponse> spaceListResponses = arSpaceData.map(arSpaceDatas -> ARSpaceListResponse.builder()
                .title(arSpaceDatas.getTitle())
                .nickname(arSpaceDatas.getUser().getNickname())
                .build());

        return spaceListResponses;
    }
}
