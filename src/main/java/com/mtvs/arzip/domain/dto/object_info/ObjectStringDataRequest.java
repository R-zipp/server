package com.mtvs.arzip.domain.dto.object_info;

import com.mtvs.arzip.domain.entity.ObjectInfo;
import com.mtvs.arzip.domain.enum_class.ObjectType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObjectStringDataRequest {

    private String objectType;
    private String objectName;

    public static ObjectInfo toEntity(ObjectStringDataRequest request) {
        return ObjectInfo.builder()
                .objectType(ObjectType.valueOf(request.getObjectType()))
                .objectName(request.getObjectName())
                .build();
    }

}
