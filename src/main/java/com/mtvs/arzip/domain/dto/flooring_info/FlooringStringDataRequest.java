package com.mtvs.arzip.domain.dto.flooring_info;

import com.mtvs.arzip.domain.entity.FlooringInfo;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlooringStringDataRequest {

    private String flooringType;

    public static FlooringInfo toEntity(FlooringStringDataRequest request) {
        return FlooringInfo.builder()
                .flooringType(request.getFlooringType())
                .build();

    }

}
