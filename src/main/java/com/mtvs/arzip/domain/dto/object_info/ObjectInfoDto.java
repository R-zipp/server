package com.mtvs.arzip.domain.dto.object_info;

import com.mtvs.arzip.domain.entity.ObjectInfo;
import com.mtvs.arzip.domain.enum_class.ObjectType;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ObjectInfoDto {

    private Long no;
    @Enumerated(EnumType.STRING)
    private ObjectType objectType;
    private String objectName;

    public static ObjectInfoDto of(ObjectInfo objectInfo) {
        return ObjectInfoDto.builder()
                .no(objectInfo.getNo())
                .objectType(objectInfo.getObjectType())
                .objectName(objectInfo.getObjectName())
                .build();
    }

}
