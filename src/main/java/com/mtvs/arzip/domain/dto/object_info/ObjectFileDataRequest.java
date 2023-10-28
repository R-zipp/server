package com.mtvs.arzip.domain.dto.object_info;

import com.mtvs.arzip.domain.entity.ObjectInfo;
import com.mtvs.arzip.domain.enum_class.ObjectType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ObjectFileDataRequest {


    private String objectImage;

    public static ObjectInfo toEntity(ObjectFileDataRequest request) {
        return ObjectInfo.builder()
                .objectImage(request.getObjectImage())
                .build();

    }

}
