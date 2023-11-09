package com.mtvs.arzip.domain.dto.wall_paper_info;

import com.mtvs.arzip.domain.entity.WallPaperInfo;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WallPaperStringDataRequest {

    private String wallPaperType;

    public static WallPaperInfo toEntity(WallPaperStringDataRequest request) {
        return WallPaperInfo.builder()
                .wallPaperType(request.getWallPaperType())
                .build();
    }

}
